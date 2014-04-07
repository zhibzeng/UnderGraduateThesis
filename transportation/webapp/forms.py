# -*- coding: utf-8 -*-
from django import forms

class UpLoadPicForm(forms.Form):
    pic=forms.FileField()
    longitude = forms.CharField(max_length=50)
    latitude = forms.CharField(max_length=50)
    notes = forms.CharField(max_length=500)
