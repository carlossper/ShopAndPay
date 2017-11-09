# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver


# Create your models here.


##########################################
####    Product Related Models
##########################################

class Brand(models.Model):
    name = models.CharField(max_length=50, blank=False)
    image_url = models.URLField(max_length=200, blank=False)


class Category(models.Model):
    name = models.CharField(max_length=50, blank=False)


class Product(models.Model):
    brand = models.ForeignKey('Brand', on_delete=models.CASCADE)
    category = models.ForeignKey('Category', on_delete=models.CASCADE)

    created = models.DateTimeField(auto_now_add=True)
    image_url = models.URLField(max_length=200, blank=False)
    name = models.CharField(max_length=50, blank=False)
    description = models.CharField(max_length=300, blank=True, default='')
    price = models.FloatField(blank=False)

    class Meta:
        ordering = ('-created',)



##########################################
###    User Related Models
##########################################


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    firstName = models.CharField(max_length=20, blank=True, default='')
    lastName = models.CharField(max_length=20, blank=True, default='')
    nif = models.CharField(max_length=20, blank=True, default='')


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        Profile.objects.create(user=instance)

@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.profile.save()




##########################################
####    Cart Related Models
##########################################


class CartProduct(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    user = models.ForeignKey('auth.User', related_name='cart', on_delete=models.CASCADE, default=1)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)

    class Meta:
        ordering = ('-created',)




##########################################
####    Invoice Related Models
##########################################


class Invoice(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    user = models.ForeignKey('auth.User', related_name='invoice', on_delete=models.CASCADE, default=1)

    class Meta:
        ordering = ('-created',)



class InvoiceProduct(models.Model):
    created = models.DateTimeField(auto_now_add=True)
    invoice = models.ForeignKey('Invoice', on_delete=models.CASCADE)
    product = models.ForeignKey('Product', on_delete=models.CASCADE)
    price = models.FloatField(blank=False, default=0)


    class Meta:
        ordering = ('-created',)