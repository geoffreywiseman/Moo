---
layout: default
title: News
---
## News
Recent news about Moo:

{% for post in site.posts %}
 - {{ post.date | date_to_long_string }} &raquo; [{{ post.title }}](/Moo/{{ post.url }})
{% endfor %}