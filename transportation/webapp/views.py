# -*- coding: utf-8 -*-
from django.shortcuts import render_to_response,render,get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib import auth
from django.template.context import RequestContext
from django.contrib.auth.decorators import login_required
from .login import LoginForm
from bs4 import BeautifulSoup
from .functions import *
import re
import datetime
from .models import Notification, Image, RoadCondition, Oil, Pic,GraphicData
from threading import Timer
import cv2
import numpy
import os,os.path
import sys
from selenium import webdriver
from .forms import UpLoadPicForm
from django.views.decorators.csrf import csrf_exempt
def main(request):
    return render_to_response('index.html', RequestContext(request))


def login(request):
    if request.method == 'GET':
        form = LoginForm()
        return render_to_response('login.html', RequestContext(request, {'form': form,}))
    else:
        form = LoginForm(request.POST)
        if form.is_valid():
            username = request.POST.get('username', '')
            password = request.POST.get('password', '')
            user = auth.authenticate(username=username, password=password)
            if user is not None and user.is_active:
                auth.login(request, user)
                return render_to_response('index.html', RequestContext(request,{'userform': form,}))
            else:
                return render_to_response('login.html', RequestContext(request, {'form': form,'password_is_wrong':True}))
        else:
            return render_to_response('login.html', RequestContext(request, {'form': form,}))




@login_required
def logout(request):
    auth.logout(request)
    return HttpResponseRedirect("/login/")





def FetchNotification(requset):
    #四川道路交通安全网 路况播报 公示公告 道路信息 出行提示
    reload(sys)
    sys.setdefaultencoding('utf8')
    domains = ['http://cd.scjtaq.com/lukuang.html','http://cd.scjtaq.com/gonggao.html','http://cd.scjtaq.com/daolu.html','http://cd.scjtaq.com/tishi.html']
    headers = {
        'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6',
        'Referer':'http://www.scjtaq.com/',
        'Origin':'http://www.scjtaq.com/',
               }
    notificationList = []
    typeindex = 1
    for domain in domains:
        page = FetchPage(domain, None, headers)
        BS_Page = BeautifulSoup(page,from_encoding="utf-8")

        # parse the date
        pattern_date = re.compile(r'(.*>)(\d+\.\d+\.\d+)(<.*)')
        spans = BS_Page.html.findAll('span')
        datelist = []
        for span in spans:
            #print(span.__str__('gb2312'))#将提取到的元素转为字符串
            #span = span.__str__('utf-8')
            span = span.__str__()
            m = pattern_date.match(span)
            if m:
                datestring = []
                datestring = re.split(r'\.', str(m.group(2)))
                datelist.append(datetime.datetime.strptime(''.join(datestring),'%Y%m%d').date())

        # parse the news links
        pattern_link = re.compile(r'(.*href=\")(http.*)(\".*target="_blank\">)(.*)(<.*)')
        newslinksdiv = BS_Page.html.find('div',{'class':'single'})
        newslinks = newslinksdiv.findAll('a')
        links = []
        newstitle = []
        for index,newslink in enumerate(newslinks):
            newslink = newslink.__str__()
            m = pattern_link.match(newslink)
            if m:
                links.append(m.group(2))
                newstitle.append(m.group(4))
        responseHtml = ''
        # capture news content
        newsContent = []
        for link in links:
            contentPage = FetchPage(link, None, headers)
            BS_content = BeautifulSoup(contentPage)
            tag = BS_content.html.find('div',{'class':'cont'})
            #print(tag.__str__('utf-8'))
            # Extract advertisement out of the content
            contentStr = tag.__str__()
            pattern_content = re.compile(r'(.*)(<div class=\"ad\"><img.*\/><\/div>)(.*)')
            m = pattern_content.match(tag.__str__())
            if m :
                #print(m.group(3))
                contentStr = m.group(1)+m.group(3)
            newsContent.append(contentStr)
            responseHtml = responseHtml+contentStr+'<br/><br/><br/>'
        graphic_count = 0
        # save notification to databases
        if len(links)==len(newstitle)==len(datelist):
            for index in range(len(links)):
                #print newstitle[index]
                # newsEntity = Notification(domain=domain, type=str(typeindex), date=datelist[index], title=newstitle[index], content=newsContent[index])
                # newsEntity.save()
                obj, created = Notification.objects.get_or_create(domain=domain, type=str(typeindex), date=datelist[index], title=newstitle[index], content=newsContent[index])
                if created:
                    graphic_count += 1
        graphic_data_entity = GraphicData(type=typeindex,count=graphic_count,time=datetime.datetime.now())
        graphic_data_entity.save()
        typeindex += 1

    responseHtml = '<html><head>xxxx</head><body>'+responseHtml+'</body></html>'
    return HttpResponse(responseHtml)





def FetchRoadCondition(request):
    #获取成都市公安局交通管理局路况信息

    def RepeatFetch():
        reload(sys)
        sys.setdefaultencoding('utf8')
        duration = 100000 # repeat fetch notification per duration seconds
        Timer(duration, RepeatFetch).start()
        print "Starting fetch roadcondition..."
        domain = 'http://www.cdjg.gov.cn/WebService/TrffYdxx.aspx'
        driver = webdriver.PhantomJS()
        driver.get(domain)
        pagelist = []
        pagelist.append(driver.page_source)
        #print driver.page_source
        divclass = driver.find_element_by_css_selector('#ContentPlaceHolder1_AspNetPager1')
        links = divclass.find_elements_by_tag_name('a');
        for i in links:
            if i.text == '下一页':
                divclass = i
        nextlink = divclass
        while nextlink.get_attribute('href'):
            nextlink.click()
            pagelist.append(driver.page_source)
            #print driver.page_source
            divclass = driver.find_element_by_css_selector('#ContentPlaceHolder1_AspNetPager1')
            links = divclass.find_elements_by_tag_name('a');
            for i in links:
                if i.text == '下一页':
                    divclass = i
            nextlink = divclass
        for page in pagelist:
            datetimeList = []
            contentList = []
            BS_Page = BeautifulSoup(page,from_encoding="utf-8")
            # parse datetime string
            timestring = BS_Page.html.findAll('div',{'class':'ydxx_time'})
            for s in timestring:
                time = datetime.datetime.strptime(s.contents[0].__str__().strip(),"%Y/%m/%d %H:%M:%S")
                datetimeList.append(time)
            # parse content string
            contentString = BS_Page.html.findAll('div',{'class':'ydxx_xx'})
            for s in contentString:
                contentList.append(s.contents[0].__str__().strip())
            # construct a roadcondition entity
            roadconditionList = []
            graphic_count = 0
            if len(contentList)==len(datetimeList):
                for index in range(len(contentList)):
                    # create or do nothing if it doesn't exists
                    obj, created = RoadCondition.objects.get_or_create(domain=domain, datetime=datetimeList[index], content=str(contentList[index]))
                    if created:
                        graphic_count += 1
                    print(contentList[index]+str(datetimeList[index]))
            graphic_data_entity = GraphicData(type=5,count=graphic_count,time=datetime.datetime.now())
            graphic_data_entity.save()
            print(len(roadconditionList))
            print('page fetch Successfully!')
            print('fetching is done!')

    RepeatFetch()
    roadconditions = RoadCondition.objects.all()
    responseHtml = 'Congratulation! You have done a good job!'
    for r in roadconditions:
        responseHtml = responseHtml+'<br/>'+r.content+'--'+str(r.datetime)
    return HttpResponse(responseHtml)





def VideoCaptureAndSavePic(request):
    directoryPath = 'F:/UndergraduateThesis/opencv/PracticeMaterials/video/'
    capturePicDir = 'F:/UndergraduateThesis/opencv/PracticeMaterials/capture/'
    for file in os.listdir(directoryPath):
        videoCapture = cv2.VideoCapture(directoryPath+file)
        if not videoCapture:
            return None
        fps = videoCapture.get(cv2.cv.CV_CAP_PROP_FPS)
        PERIOD = fps*60*3 # Capture video per 10s
        size = (int(videoCapture.get(cv2.cv.CV_CAP_PROP_FRAME_WIDTH)),
            int(videoCapture.get(cv2.cv.CV_CAP_PROP_FRAME_HEIGHT)))
        print(PERIOD)
        print(int(PERIOD))
        fileName = os.path.splitext(file)[0]
        subDirectory = capturePicDir+fileName
        if not os.path.exists(subDirectory):
            os.makedirs(subDirectory)
        count = 1
        while True:
            print('#'+str(count)+'starting capturing...')
            success, frame = videoCapture.read()
            for i in range(1,int(PERIOD)):
                success, frame = videoCapture.read()
                if not success:
                    return None
            cv2.imwrite(subDirectory+'/'+datetime.datetime.now().strftime('%Y%m%d%H%M%S')+'.jpg',frame)
            count += 1
            if count == 10:
                break;
    return render_to_response('index.html', RequestContext(request))


# using webcrawler to scraping oil price
def OilPrice(request):
    driver = webdriver.PhantomJS()
    driver.get('http://www.bitauto.com/youjia/')
    html_source = driver.page_source
    #print html_source
    soup = BeautifulSoup(html_source)
    sichuan = soup.select("body > div.bt_page > div.bt_page > div > div.oilTableOut > table > tbody > tr:nth-of-type(11) > th:nth-of-type(2) > a")
    #90号汽油
    type1 = soup.select("body > div.bt_page > div.bt_page > div > div.oilTableOut > table > tbody > tr:nth-of-type(11) > td:nth-of-type(5)")
    #93号汽油
    type2 = soup.select("body > div.bt_page > div.bt_page > div > div.oilTableOut > table > tbody > tr:nth-of-type(11) > td:nth-of-type(6)")
    #97号汽油
    type3 = soup.select("body > div.bt_page > div.bt_page > div > div.oilTableOut > table > tbody > tr:nth-of-type(11) > td:nth-of-type(7)")
    #0号柴油(元/升)
    type4 = soup.select("body > div.bt_page > div.bt_page > div > div.oilTableOut > table > tbody > tr:nth-of-type(11) > td:nth-of-type(8)")
    sichuan = str(sichuan[0].get_text())
    type1 = str(type1[0].get_text())
    type2 = str(type2[0].get_text())
    type3 = str(type3[0].get_text())
    type4 = str(type4[0].get_text())
    print sichuan.encode('utf8')
    oilprice = Oil(city_name=sichuan.encode('utf8'),typeone_price=type1, typetwo_price=type2, typethree_price=type3, typefour_price=type4, date=datetime.datetime.now().date())
    oilprice.save()
    print driver.current_url
    driver.quit
    responseHtml = '<html><body>Scraping Oil Price Successfully!</body></html>'
    return HttpResponse(responseHtml)


# Upload file function
@csrf_exempt
def uploadPic(request):
    if request.method=="POST":
        form=UpLoadPicForm(request.POST,request.FILES)
        #if form.is_valid():
        longitude = request.POST.get('longitude', '')
        latitude = request.POST.get('latitude', '')
        notes = request.POST.get('notes', '')
        print notes
        time = datetime.datetime.now()
        print longitude
        print latitude
        print notes
        print datetime
        newdoc=Pic(pic=request.FILES["pic"],longitude=longitude,latitude=latitude,notes=notes,datetime=time)
        newdoc.save()
        return HttpResponse("success")
    else:
        form=UpLoadPicForm()
        print('get request')
    return render_to_response('upload_image.html',{"form":form},context_instance=RequestContext(request))



def GraphicDataFunc(request):
    datalistsone = []
    dataliststwo = []
    dataliststhree = []
    datalistsfour = []
    datalistsfive = []
    for index in [1,2,3,4,5]:
        graphic_data = []
        graphic_data = GraphicData.objects.filter(type=index).order_by('-id')[:10]
        for data in graphic_data:
            if index == 1:
                datalistsone.append(int(data.count))
            elif index==2:
                dataliststwo.append(int(data.count))
            elif index==3:
                dataliststhree.append(int(data.count))
            elif index==4:
                datalistsfour.append(int(data.count))
            else:
                datalistsfive.append(int(data.count))
    print datalistsone
    print dataliststwo
    print dataliststhree
    print datalistsfour
    print datalistsfive
    return render_to_response('graphic_data.html', RequestContext(request,{'datalistsone': datalistsone,
        'dataliststwo':dataliststwo,'dataliststhree':dataliststhree,'datalistsfour':datalistsfour,
        'datalistsfive':datalistsfive,}))





