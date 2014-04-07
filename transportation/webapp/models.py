# -*- coding: utf-8 -*-
from django.db import models
from django.contrib import admin
# Create your models here.

class Notification(models.Model):
    """docstring for Notification"""
    domain = models.CharField(max_length=50)
    type = models.IntegerField()
    date = models.DateField()
    content = models.TextField()
    title = models.CharField(max_length=300)
    def __unicode__(self):
        return u'%s' % (self.title)

class RoadCondition(models.Model):
    """docstring for Notification"""
    domain = models.CharField(max_length=50)
    datetime = models.DateTimeField()
    content = models.TextField()
    def __unicode__(self):
        return u'%s' % (self.content)

class Image(models.Model):
    type = models.IntegerField()
    name = models.CharField(max_length=100)
    path = models.CharField(max_length=100)
    pic_id = models.IntegerField()
    pic_type = models.IntegerField()
    def __unicode__(self):
        return u'%s' % (self.name)

class Oil(models.Model):
    """docstring for OilPrice"""
    city_name = models.CharField(max_length=100)
    typeone_price = models.CharField(max_length=50)
    typetwo_price = models.CharField(max_length=50)
    typethree_price = models.CharField(max_length=50)
    typefour_price = models.CharField(max_length=50)
    date = models.DateField()
    def __unicode__(self):
        return u'%s' % (self.city_name)

class Pic(models.Model):
    #这里的保存目录就是/mdeia.documents/%Y
    pic=models.FileField(upload_to='documents/%Y%m%d')
    longitude = models.CharField(max_length=50)
    latitude = models.CharField(max_length=50)
    datetime = models.DateTimeField()
    notes = models.TextField()
