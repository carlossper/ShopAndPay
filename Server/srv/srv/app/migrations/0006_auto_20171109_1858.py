# -*- coding: utf-8 -*-
# Generated by Django 1.9.7 on 2017-11-09 18:58
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0005_invoiceproduct_price'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='cartproduct',
            options={'ordering': ('-created',)},
        ),
        migrations.AlterModelOptions(
            name='invoice',
            options={'ordering': ('-created',)},
        ),
        migrations.AlterModelOptions(
            name='invoiceproduct',
            options={'ordering': ('-created',)},
        ),
        migrations.AlterModelOptions(
            name='product',
            options={'ordering': ('-created',)},
        ),
    ]