# -*- coding: utf-8 -*-
from django.contrib import admin
from .models import Notification,Image,RoadCondition,Oil
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
    """docstring for ImageAdmin"""
    list_display = ('domain', 'datetime', 'content')
    search_fields = ('content','datetime','domain',)
    fields=('domain','content','datetime')#依据domain、content、datetime排序显示
    ordering = ('datetime',)

class OilAdmin(admin.ModelAdmin):
    """docstring for ImageAdmin"""
    list_display = ('city_name', 'typeone_price', 'typetwo_price', 'typethree_price', 'typefour_price', 'date')
    fields=('date','city_name')#依据domain、content、datetime排序显示
    ordering = ('date',)


admin.site.register(Notification, NotificationAdmin)
admin.site.register(Image, ImageAdmin)
admin.site.register(RoadCondition, RoadConditionAdmin)
admin.site.register(Oil, OilAdmin)

