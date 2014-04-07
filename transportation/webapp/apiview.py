# -*- coding: utf-8 -*-
from django.shortcuts import render_to_response,render,get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.template.context import RequestContext
from .models import Notification,RoadCondition,Oil,Pic
from django.utils import simplejson
import datetime

def ApiNotificationList(request):
     #types = Notification.objects.values_list('type', flat=True).distinct()
    type = 1
    if request.method == 'GET':
        type = request.GET.get('type',1)
    notificationList = {}
    notifications=[]
    entities = Notification.objects.filter(type=type).order_by('-date')[:5]
    for entity in entities:
        notification = {}
        notification['type'] = entity.type
        notification['date'] = str(entity.date)
        notification['title'] = entity.title
        notification['content'] = entity.content
        notifications.append(notification)
    notificationList['rows'] = notifications
    j = simplejson.dumps(notificationList,ensure_ascii=False)
    return HttpResponse(j)

def ApiRoadConditionList(request):
    RoadConditionList = {}
    RoadConditions=[]
    entities = RoadCondition.objects.order_by('-datetime')[:10]
    for entity in entities:
        roadcondition = {}
        roadcondition['datetime'] = str(entity.datetime)
        roadcondition['content'] = entity.content
        RoadConditions.append(roadcondition)
    RoadConditionList['info'] = RoadConditions
    j = simplejson.dumps(RoadConditionList,ensure_ascii=False)
    return HttpResponse(j)

def ApiOilPrice(request):
    entities = Oil.objects.all().order_by('-date')[:1]
    oilprice = {}
    for entity in entities:
        oilprice['city_name'] = str(entity.city_name)
        oilprice['typeone_price'] = entity.typeone_price
        oilprice['typetwo_price'] = entity.typetwo_price
        oilprice['typethree_price'] = entity.typethree_price
        oilprice['typefour_price'] = entity.typefour_price
    j = simplejson.dumps(oilprice,ensure_ascii=False)
    return HttpResponse(j)

def ApiPicList(request):
    entities = Pic.objects.filter(datetime__gte=datetime.date.today()).order_by('-datetime')
    PicList = {}
    Pics=[]
    for entity in entities:
        pic_entity = {}
        pic_entity['pic'] = str(entity.pic)
        pic_entity['longitude'] = entity.longitude
        pic_entity['latitude'] = entity.latitude
        pic_entity['datetime'] = str(entity.datetime)
        pic_entity['notes'] = entity.notes
        Pics.append(pic_entity)
    PicList['info'] = Pics
    j = simplejson.dumps(PicList,ensure_ascii=False)
    return HttpResponse(j)
