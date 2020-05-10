# jsoup
java爬虫jsoup,通过csdn专栏地址解析专栏里面的每一篇文档的标题和网址，方便后续汇总

>程序猿学社的GitHub，欢迎**Star**
[github技术专题](https://github.com/ITfqyd/cxyxs)
本文已记录到github

@[toc]
# 前言
>  从19年开始，社长，就在写系列文章，整个系列写完后，就需要写一篇总结的文章，需要把所有的文章归纳进去。相信不少童鞋都会有这种困惑。

# 需求
通过csdn系列文章的网站，采用jsoup，输出该系列文章的所有标题和url地址。使用MD方式网址输出
![](https://img-blog.csdnimg.cn/20200510191137621.png)
# 要求
- 需要有一定的css、html、js前端基础
- 有一定的java基础
# api接口文档
## 查找元素
|方法| 描述 |
|--|--|
| getElementById(String id) | 根据id选择器获取Element |
|getElementsByTag(String tagName)|通过标签过滤获取Element|
|getElementsByClass(String className)|通过类样式选择器获取Element|
|getElementsByAttribute(String key)|通过属性名获取Element|
## 获取元素的值
|方法| 描述 |
|--|--|
|attr(String attributeKey)|获取属性的值|
|attr(String attributeKey, String attributeValue)|给属性赋值|
|text()|获取对应的值，只是单纯的值|
|html()|获取对应的html值，包含标签|

# 实战
## pom.xml
```xml
  <!--爬取数据关键jsoup -->
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.10.3</version>
        </dependency>
```
- 导入jsoup依赖
## 代码

```java
package com.cxyxs.jsoup.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;

/**
 * Description：测试
 * Author: 程序猿学社
 * Date:  2020/5/10 18:07
 * Modified By:
 */
public class JsoupUtils {
    public static void main(String[] args) throws  Exception {
        getList("https://blog.csdn.net/qq_16855077/category_9722768.html");
    }

    /**
     * 通过csdn的系列文章网址输出对应的内容
     * @param url
     * @throws Exception
     */
    public static void getList(String url)throws  Exception{
        //网址
        Document document = Jsoup.connect(url).get();

        // 获取标题
        //String title = document.title();
        //获取头部信息
        //Element head = document.head();
        //取到菜单列表ul
        Elements elements = document.getElementsByClass("column_article_list");
        //取出每个li
        Elements elementsByTag = elements.get(0).getElementsByTag("li");
        for (Element el : elementsByTag) {
            //取出每个li元素的href
            String href = el.getElementsByTag("a").attr("href");
            //先取出h2标签
            String titleDiv = el.getElementsByTag("h2").text();
            String[] titles = titleDiv.split(" ");
            System.out.println("!["+titles[1]+"]("+href+")");
        }
    }
}
```
![](https://img-blog.csdnimg.cn/20200510195547304.png)
到这里，我们就实现了通过csdn列表的网址，输出markdown的列表的内容格式。是不是感觉很so easy哦？
- 也有不少人通过httpclient方法实现，当然，通过这种方法也可以实现。说说实现的大致思路。首先通过网址获取整个html，而且通过自己各种写判断处理，因太麻烦，**不建议采用这种方式**。

**看到这里是不是一脸懵逼**？别急，我们一步一步的分析一下社长的代码,**为什么要这样编写**。
# 分析
## **第一步**
```java
Document document = Jsoup.connect(url).get();
```
- 这里的document，我们通过syso输出，可以知道实际上，就是把url对应网址输出。等同于，我们在网页输入F12，进入开发者调试模式
![](https://img-blog.csdnimg.cn/20200510200817178.png)
- 输出的内容就是整个html页面，也就是红色区域的内容。
- 到这一步，我们已经实现，取到整个html页面。
## 第二步 获取文章列表的ul
![](https://img-blog.csdnimg.cn/20200510201133586.png)
- 点击红色区域的图标，把他移动到，我们我们需要选择的标题这一行上面。
![](https://img-blog.csdnimg.cn/20200510201341550.png)
- 分析csdn前端开发是如何显示文章列表的
- 通过分析，可以看出，最外层是ul，里面的每一篇文章的显示都是一个li。例如10篇文章，就有10个li。
![](https://img-blog.csdnimg.cn/2020051020164343.png)
```java
//取到菜单列表ul
Elements elements = document.getElementsByClass("column_article_list");
```
这里为什么要调用**getElementsByClass**方法？
-  ul标签上是class属性，值为column_article_list，通过社长之前的api接口文档，可知，class选择器需要调用getElementsByClass。
- 到这里，我们已经实现从整个html变为只获取ul的内容。万里长征又进了一步。
## 第三步 获取文章的每个li
```java
  //取出每个li
        Elements elementsByTag = elements.select("li");
        for (Element el : elementsByTag) {
            //取出每个li元素的href
            String href = el.getElementsByTag("a").attr("href");
            //先取出h2标签
            String titleDiv = el.getElementsByTag("h2").text();
            String[] titles = titleDiv.split(" ");
            //![描述](网址)
            System.out.println("!["+titles[1]+"]("+href+")");
        }
```
- elements.**select**("li")  获取所有li标签的html元素
一步一步再来剖析
```java
Elements elementsByTag = elements.select("li");
```
- 在ul中，获取li，实际上返回如下
![](https://img-blog.csdnimg.cn/20200510202803819.png)
分析单个的li，发现只需要取到href对应的属性值，h2标签就可以实现我们的功能。
![](https://img-blog.csdnimg.cn/20200510202913265.png)
**获取href的值**
```java
 String href = el.getElementsByTag("a").attr("href");
```
- getElementsByTag获取a标签这个dom元素
- attr("href")为获取当前dom元素属性为href的值

**获取标题的值**
```java
 //先取出h2标签
String titleDiv = el.getElementsByTag("h2").text();
String[] titles = titleDiv.split(" ");
```
![](https://img-blog.csdnimg.cn/2020051020361078.png)
- titles[1] 为文章标题的值 
**获取标题的值，为什么要这些写？**
通过查看html代码，发现h2里面有一个span标签，里面的值为文章类型(原创、转载)，紧接着就是文字标题。
- 没有办法，通过标签选择器筛选到标题的值，社长想到的方法，获取h2的text的值，这样我们去到的内容为"原创 [多线程XXX] 十四面试官说：说说java中XXXX"
- 把这个字符串按空格切割，取索引为1的值，就是我们的标题
# 结论
本文最好有一定的前端基础，不然，看着，会有点懵逼。如果你懂js、css、html，你再去看jsoup提供的api接口，会发现，几乎完全类似。

---
本文仅供学习，读取个人博客专栏的一个地址，获取每个文章的标题和网址，方便专栏文章汇总。

>作者：**程序猿学社**
>原创公众号：『**程序猿学社**』，专注于java技术栈，分享java各个技术系列专题，以及各个技术点的面试题。
原创不易，转载请注明来源(注明：来源于公众号：**程序猿学社**， 作者：**程序猿学社**)。 

