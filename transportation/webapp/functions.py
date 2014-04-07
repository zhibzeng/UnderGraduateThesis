# -*- coding: utf-8 -*-
__author__ = 'Administrator'
import urllib
import urllib2
import cookielib
import io
import gzip
def FetchPage(url,data,headers):
    """ Fetch page, data type is dict"""
    postdata = None
    cookieJar = cookielib.CookieJar()
    opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookieJar))
    if not (data==None) :
        print(data)
        postdata = urllib.urlencode(data)
        postdata = postdata.encode('UTF-8')
        print(postdata)
    if not (postdata==None):
        req = urllib2.Request(
                headers = headers,
                url = url,
                data = postdata
            )
    else:
        req = urllib2.Request(
                headers = headers,
                url = url
            )
    try:
        page = opener.open(req)
    except:
        print('fetch page error')
    ## using gzip to fetch page
    if page.code == 200:
        predata = page.read().decode('utf-8')
        pdata = io.StringIO(predata)
        gzipper = gzip.GzipFile(fileobj = pdata)
        try:
            pagedata = gzipper.read()
            print('gzip')
        except:
            # if the server don't support gzip download directly
            pagedata = predata
    else:
        return None
    page.close()
    return pagedata

