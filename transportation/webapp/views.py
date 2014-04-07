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
from .models import Notification, Image, RoadCondition, Oil, Pic
from threading import Timer
import cv2
import numpy
import os,os.path
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
    #四川道路交通安全网 路况播报 公示公告 道路信息
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
        BS_Page = BeautifulSoup(page)

        # parse the date
        pattern_date = re.compile(r'(.*>)(\d+\.\d+\.\d+)(<.*)')
        spans = BS_Page.html.findAll('span')
        datelist = []
        for span in spans:
            #print(span.__str__('gb2312'))#将提取到的元素转为字符串
            span = span.__str__('utf-8')
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
            newslink = newslink.__str__('utf-8')
            m = pattern_link.match(newslink)
            if m:
                links.append(m.group(2))
                newstitle.append(m.group(4))
        responseHtml = ''
        # capture news content
        newsContent = []
        for link in links:
            contentPage = FetchPage(link, None, headers)
            BS_content = BeautifulSoup.BeautifulSoup(contentPage)
            tag = BS_content.html.find('div',{'class':'cont'})
            #print(tag.__str__('utf-8'))
            # Extract advertisement out of the content
            contentStr = tag.__str__('utf-8')
            pattern_content = re.compile(r'(.*)(<div class=\"ad\"><img.*\/><\/div>)(.*)')
            m = pattern_content.match(tag.__str__('utf8'))
            if m :
                #print(m.group(3))
                contentStr = m.group(1)+m.group(3)
            newsContent.append(contentStr)
            responseHtml = responseHtml+contentStr+'<br/>++++++++++++++++++++++++++++++++++++++++++++++++++++++++<br/><br/>'
        # save notification to databases
        if len(links)==len(newstitle)==len(datelist):
            for index in range(len(links)):
                #print newstitle[index]
                newsEntity = Notification(domain=domain, type=str(typeindex), date=datelist[index], title=newstitle[index], content=newsContent[index])
                newsEntity.save()
        typeindex += 1

    responseHtml = '<html><head>xxxx</head><body>'+responseHtml+'</body></html>'
    return HttpResponse(responseHtml)


def FetchRoadCondition(request):
    #获取成都市公安局交通管理局路况信息
    #fetchTimes = 0 # 第几次获取数据
    totalPages = 3 # 一共要获取几个页面数据
    def RepeatFetch():
        duration = 100000 # repeat fetch notification per duration seconds
        Timer(duration, RepeatFetch).start()
        print "Starting fetch roadcondition..."
        for pageNum in range(totalPages):
            print('Starting fetch No.'+str(pageNum)+' page...')
            domain = 'http://www.cdjg.gov.cn/WebService/TrffYdxx.aspx'
            headers = {
                'User-Agent':'Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6',
                'Referer':'http://www.cdjg.gov.cn/WebService/TrffYdxx.aspx',
                'Origin':'http://www.cdjg.gov.cn',
                       }
            postdata = {
                '__VIEWSTATE':'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',
                '__EVENTTARGET':'ctl00$ContentPlaceHolder1$AspNetPager1',
                '__EVENTARGUMENT':str(pageNum),
                '__EVENTVALIDATION':'/wEdAAJ6YOkLMRh6X6WbncTIi4vT5UDzBtAvn5Vse6EOGRvssu3WfuOq+jTvpo7XLH+qFlwueVrFTA9B5Sfvtf9/hBPa',
                'ctl00$ContentPlaceHolder1$hidXxlx':''
                 }
            page = FetchPage(domain, postdata, headers)
            datetimeList = []
            contentList = []
            BS_Page = BeautifulSoup(page)
            # parse datetime string
            timestring = BS_Page.html.findAll('div',{'class':'ydxx_time'})
            for s in timestring:
                time = datetime.datetime.strptime(s.contents[0].__str__('utf8').strip(),"%Y/%m/%d %H:%M:%S")
                datetimeList.append(time)
            # parse content string
            contentString = BS_Page.html.findAll('div',{'class':'ydxx_xx'})
            for s in contentString:
                contentList.append(s.contents[0].__str__('utf8').strip())
            # construct a roadcondition entity
            roadconditionList = []
            if len(contentList)==len(datetimeList):
                for index in range(len(contentList)):
                    roadcondition = RoadCondition(domain=domain, datetime=datetimeList[index], content=contentList[index])
                    roadconditionList.append(roadcondition)
                    roadcondition.save()
                    print(contentList[index]+str(datetimeList[index]))
            print(len(roadconditionList))
            print('No.'+str(pageNum)+' page fetch Successfully!')
        #fetchTimes += 1
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







