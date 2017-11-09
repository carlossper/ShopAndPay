# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

# Create your views here.
from django.contrib.auth.models import User, Group
from rest_framework import viewsets
from serializers import *
from models import *
from rest_framework.permissions import IsAuthenticated

from rest_framework.decorators import api_view, permission_classes

from rest_framework.authentication import TokenAuthentication
from rest_framework.views import APIView
from rest_framework import authentication, permissions

from  validators import *

from permissions import *

from rest_framework.response import Response
from rest_framework import status

from rest_framework import mixins


from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from serializers import ProductSerializer


from django.core import serializers

from rest_framework.permissions import AllowAny

import json


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    permission_classes = [AllowAny]

    queryset = User.objects.all().order_by('-date_joined')
    serializer_class = UserSerializer


class BrandViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    permission_classes = [AllowAny]
    queryset = Brand.objects.all()
    serializer_class = BrandSerializer


class CategoryViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    permission_classes = [AllowAny]
    queryset = Category.objects.all()
    serializer_class = CategorySerializer


class ProfileViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    permission_classes = [AllowAny]
    queryset = Profile.objects.all()
    serializer_class = UserSerializer




class ProductViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    permission_classes = [AllowAny]
    queryset = Product.objects.all()
    serializer_class = ProductSerializer



class InvoiceProductViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    permission_classes = [AllowAny]
    queryset = InvoiceProduct.objects.all()
    serializer_class = InvoiceProductSerializer



@csrf_exempt
def product_list(request):
    """
    List all products, or create a new product.
    """
    if request.method == 'GET':
        snippets = Product.objects.all()
        serializer = ProductSerializer(snippets, many=True)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = ProductSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)


"""
@csrf_exempt
def product_detail(request, pk):
"""
   # Retrieve, update or delete a product.
"""

    try:
        product = Product.objects.get(pk=pk)
    except Product.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = ProductSerializer(product)
        return JsonResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = ProductSerializer(product, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        product.delete()
        return HttpResponse(status=204)

"""

@csrf_exempt
@api_view(['GET'])
@permission_classes((IsAuthenticated, ))
def product_detail(request, pk):
    authentication_classes = (TokenAuthentication,)

    if request.method == 'GET':
        if request.user.is_authenticated:
            product = Product.objects.get(id=pk)
            #data = serializers.serialize('json', products)
            #data = []
            dict = {}

            dict['id'] = product.id
            dict['image_url'] = product.image_url
            dict['name'] = product.name
            dict['description'] = product.description
            dict['price'] = product.price
            dict['brand'] = product.brand.name
            dict['category'] = product.category.name

            #data.append(dict)

            dict = json.dumps(dict)

            print pk
            print request.user
            print dict

            return HttpResponse(dict, "application/json")
        else:
            print pk
            print request.user
            return HttpResponse(status=403)



#####################################
##CART
######################################


class ManageCartViewSet(mixins.CreateModelMixin,
                  mixins.RetrieveModelMixin,
                  #mixins.UpdateModelMixin,
                  mixins.DestroyModelMixin,
                  mixins.ListModelMixin,
                  viewsets.GenericViewSet):
    """
    This viewset provides `create` and `destroy` actions.
    """
    queryset = CartProduct.objects.all()
    serializer_class = CartProductSerializer
    permission_classes = [IsOwnerOrReadOnly]


    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        headers = self.get_success_headers(serializer.data)
        return Response(serializer.data, status=status.HTTP_201_CREATED, headers=headers)












@csrf_exempt
def process_payment(request):
    """
    List all code snippets, or create a new snippet.
    """

    if request.method == 'POST':
        data = JSONParser().parse(request)
        print "#################################"
        print "Process Payment"
        print "#################################"
        if 'product' in data:
            print data['product']

        if 'card-number' in data:
            if is_valid_card_number(data['card-number']):
                queryset = CartProduct.objects.filter(user=request.user.id)
                data2 = serializers.serialize('json', queryset)
                serializer_class = CartProductSerializer

                invoice = Invoice(user=request.user)
                invoice.save()

                for data in queryset:
                    invoiceProduct = InvoiceProduct(invoice=invoice, product=data.product, price=data.product.price)
                    invoiceProduct.save()
                    #print data.created

                return HttpResponse(data2, "application/json")
            else:
                return HttpResponse("Invalid Card Number", status=400)
        else:
            return HttpResponse("Card Number not received", status=400)



        #data.set('user', request.user)
        #serializer = CartProductSerializer(data=data)
        #if serializer.is_valid():
        #    serializer.save()
        #    return JsonResponse(serializer.data, status=201)
        return HttpResponse(status=200)



@csrf_exempt
def cart_detail(request, pk):
    """
         Retrieve, update or delete a code snippet.
    """
    try:
        product = CartProduct.objects.get(pk=pk)
    except Product.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'DELETE':
        product.delete()
        return HttpResponse(status=204)




@csrf_exempt
def user_invoices(request):
    """
    Retrieve, update or delete a product.
    """

    if request.method == 'GET':
        if request.user.is_authenticated():
            invoice = Invoice.objects.filter(user=request.user.id)
            data = serializers.serialize('json', invoice)

            return HttpResponse(data, "application/json")
        else:
            return HttpResponse(status=404)



@csrf_exempt
@api_view(['GET'])
@permission_classes((IsAuthenticated, ))
def cart_list(request):
    """
    Retrieve, update or delete a product.
    """
    #permission_classes = (AllowAny,)


    if request.method == 'GET':
        if request.user.is_authenticated():
            productList = CartProduct.objects.filter(user=request.user.id)
            #data = serializers.serialize('json', products)

            data = []
            for val in productList:
                dict = {}
                product = Product.objects.get(id=val.product.id)

                dict['id'] = product.id
                dict['image_url'] = product.image_url
                dict['name'] = product.name
                dict['price'] = product.price
                dict['brand'] = product.brand.name
                dict['category'] = product.category.name
                dict['cpid'] = val.id

                data.append(dict)

            data = json.dumps(data)

            return HttpResponse(data, "application/json")
        else:
            return HttpResponse(status=402)


class ListCart(APIView):
    """
    View to list all users in the system.

    * Requires token authentication.
    * Only admin users are able to access this view.
    """
    authentication_classes = (authentication.TokenAuthentication,)
    #permission_classes = (permissions.IsAdminUser,)

    def get(self, request, format=None):
        """
        Return a list of all users.
        """
        productList = CartProduct.objects.filter(user=request.user.id)
        data = []
        for val in productList:
            dict = {}
            product = Product.objects.get(id=val.product.id)

            dict['id'] = product.id
            dict['image_url'] = product.image_url
            dict['name'] = product.name
            dict['price'] = product.price
            dict['brand'] = product.brand.name
            dict['category'] = product.category.name

            data.append(dict)

        data = json.dumps(data)

        return HttpResponse(data, "application/json")



@csrf_exempt
def invoice(request, pk):

    if request.method == 'GET':
        if request.user.is_authenticated():
            invoice = InvoiceProduct.objects.filter(invoice=pk)
            data = []
            for val in invoice:
                dict = {}
                product = Product.objects.get(id=val.product.id)

                dict['id'] = product.id
                dict['image_url'] = product.image_url
                dict['name'] = product.name
                dict['price'] = product.price
                dict['brand_name'] = product.brand.name

                data.append(dict)

            data = json.dumps(data)

            return HttpResponse(data, "application/json")