# -*- coding: utf-8 -*-
# Generated by Django 1.11.6 on 2017-10-30 17:01
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0004_auto_20171029_0023'),
    ]

    operations = [
        migrations.AddField(
            model_name='invoiceproduct',
            name='price',
            field=models.FloatField(default=0),
        ),
    ]
