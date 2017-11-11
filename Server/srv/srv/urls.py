"""srv URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin


from django.conf.urls import url, include
from rest_framework import routers
from app import views

router = routers.DefaultRouter()
router.register(r'users', views.UserViewSet)
router.register(r'profiles', views.ProfileViewSet)


router.register(r'brands', views.BrandViewSet)
router.register(r'categories', views.CategoryViewSet)
router.register(r'manage-cart', views.ManageCartViewSet)
router.register(r'products', views.ProductViewSet)

router.register(r'inv-prod', views.InvoiceProductViewSet)


# Wire up our API using automatic URL routing.
# Additionally, we include login URLs for the browsable API.
urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),

    url(r'^rest-auth/', include('rest_auth.urls')),
    url(r'^rest-auth/registration/', include('rest_auth.registration.urls')),
    url(r'^account/', include('allauth.urls')),
    url(r'^admin/', include(admin.site.urls)),

    url(r'^process-payment/$', views.process_payment),
    url(r'^user-invoices/$', views.user_invoices),

    url(r'^invoice/(?P<pk>[0-9]+)/$', views.invoice),

    #url(r'^products2/(?P<pk>[0-9]+)/$', views.product_detail),

    url(r'^cart-list/$', views.cart_list),
    url(r'^cart-details/(?P<pk>[0-9]+)/$', views.cart_detail),
    url(r'^product-details/(?P<pk>[0-9]+)/$', views.product_detail),
    url(r'^products-by-category/(?P<pk>[0-9]+)/$', views.products_by_category),

]
