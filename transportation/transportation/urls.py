from django.conf.urls import patterns, include, url
import settings
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'transportation.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
    url(r'^grappelli/',include('grappelli.urls')),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^login/$',  'webapp.views.login', name='login'),
    url(r'^logout/$', 'webapp.views.logout', name='logout'),
    url(r'^FetchNotification/$', 'webapp.views.FetchNotification',name='FetchNotification'),
    url(r'^FetchRoadCondition/$', 'webapp.views.FetchRoadCondition',name='FetchRoadCondition'),
    url(r'^VideoCapture/$','webapp.views.VideoCaptureAndSavePic',name='VideoCapture'),
    url(r'^api/notificationlist/$','webapp.apiview.ApiNotificationList',name='ApiNotificationList'),
    url(r'^api/roadConditionlist/$','webapp.apiview.ApiRoadConditionList',name='ApiRoadConditionList'),
    url(r'^OilPrice/$','webapp.views.OilPrice',name='OilPrice'),
    url(r'^api/oilPricelist/$','webapp.apiview.ApiOilPrice',name='ApiOilPrice'),
    url(r'^api/piclist/$','webapp.apiview.ApiPicList',name='ApiPicList'),
    url(r'^api/crossingList/$','webapp.apiview.ApiCrossingList',name='ApiCrossingList'),
    url(r'^upload/image/$','webapp.views.uploadPic',name='uploadimage'),
    url(r'^media/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.MEDIA_ROOT,}),
    url(r'^graphic/data/$','webapp.views.GraphicDataFunc',name='GraphicDataFunc'),
    url(r'','webapp.views.main',name='main'),
)
