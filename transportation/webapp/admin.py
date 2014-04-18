# -*- coding: utf-8 -*-
from django.contrib import admin
from .models import Notification,Image,RoadCondition,Oil,CrossingSpeed,Crossing,Pic,GraphicData
# Register your models here.
class NotificationAdmin(admin.ModelAdmin):
    """docstring for NotificationAdmin"""
    list_display = ('domain', 'type', 'date', 'content', 'title')
    list_filter = ('domain',)
    date_hierarchy = 'date'
    ordering = ('-date',)


class ImageAdmin(admin.ModelAdmin):
    """docstring for ImageAdmin"""
    list_display = ('type', 'name', 'path', 'pic_id', 'pic_type')
    search_fields = ('name',)
    ordering = ('-type', '-pic_id')


class RoadConditionAdmin(admin.ModelAdmin):
    """docstring for RoadConditionAdmin"""
    list_display = ('domain', 'datetime', 'content')
    search_fields = ('content','datetime','domain',)
    fields=('domain','content','datetime')#依据domain、content、datetime排序显示
    ordering = ('datetime',)

class OilAdmin(admin.ModelAdmin):
    """docstring for OilAdmin"""
    list_display = ('city_name', 'typeone_price', 'typetwo_price', 'typethree_price', 'typefour_price', 'date')
    fields=('date','city_name')#依据domain、content、datetime排序显示
    ordering = ('date',)

class CrossingAdmin(admin.ModelAdmin):
    """docstring for CrossingAdmin"""
    list_display = ('name', 'type', 'longitude', 'latitude','labelid')
    list_filter = ('type','name',)
    ordering = ('type',)

class CrossingSpeedAdmin(admin.ModelAdmin):
    """docstring for CrossingSpeedAdmin"""
    list_display = ('origin', 'destination', 'type', 'speed_num')
    list_filter = ('type',)
    ordering = ('type',)

class PicAdmin(admin.ModelAdmin):
    """docstring for PicAdmin"""
    list_display = ('pic', 'longitude', 'latitude', 'datetime','notes')
    list_filter = ('notes',)
    ordering = ('-datetime',)

class GraphicDataAdmin(admin.ModelAdmin):
    """docstring for GraphicDataAdmin"""
    list_display = ('type', 'count', 'time')
    list_filter = ('type',)
    ordering = ('-time',)





admin.site.register(Notification, NotificationAdmin)
admin.site.register(Image, ImageAdmin)
admin.site.register(RoadCondition, RoadConditionAdmin)
admin.site.register(Oil, OilAdmin)
admin.site.register(Crossing, CrossingAdmin)
admin.site.register(CrossingSpeed, CrossingSpeedAdmin)
admin.site.register(Pic,PicAdmin)
admin.site.register(GraphicData,GraphicDataAdmin)

