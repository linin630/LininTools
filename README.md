LininTools
==========

邻音努力收集的一些工具类~也有一些自己写的

PS：因为邻音比较懒，所以之前做这个项目的时候并没有写什么文档，下面这些也是最近才写的，没有多少，将就着看吧。

--------简单使用说明---------
1、Wheel：布局里用WheelView，在代码里findview后使用WheelView的setViewAdapter方法，取数据时用getCurrentItem()。
2、SlidingMenu：继承SlidingFragmentActivity，初始化SlidingMenu sm = getSlidingMenu();并设置（设置的方法可以到SlidingMenu类里面找，有说明），设置的顺序如下：
		sm = getSlidingMenu();
		setContentView(R.layout.activity_main);// 设置当前的视图
		setBehindContentView(getMenu());// 设置左页视图
		sm.setMode(SlidingMenu.LEFT);
		打开和关闭菜单使用sm.toggle();
3、数据库查询order排序：order by XX desc(降序) | asc(升序)

4、瀑布流简易使用，用法跟ListView一样，xml写成如下即可
    <com.huewu.pla.lib.MultiColumnListView
        xmlns:pla="http://schemas.android.com/apk/res-auto"
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" 
        pla:plaColumnNumber="2"
        pla:plaLandscapeColumnNumber="3"
        />

-----------计划加上的功能--------------

优化数据库，因为访问数据库是费时操作，所以要做成异步，并提供回调接口

访问网络时，可能因为断网而没有提交，做个下次启动就自动提交的功能吧

做个特殊的listview，提供一个方法可以在调用时，让某个item铺满屏幕，效果类似百度贴吧的回复

TimeUtil的start最好传入一个String，然后跟end传入的String一样才算是同一段费时代码

HVListView用法太麻烦，需要优化成像ListView一样方便使用的控件。并且最好做多个扩展功能，可以多级表头之类的。

做一个service服务类来用于访问网络，功能规划：
1、addUrl添加要访问的连接，服务类并不会立即访问，而是存起来，定时访问一个连接（先来后到）。
2、

Notification工具类的用法不够完善，只是简单的使用，需改进

TableView自定义表格，正在测试中，首行固定、首列固定功能未做

-----------功能更新--------------
2014.07.09 访问网络的工具类NetAcess加上设置代理功能（未测试）
2014.07.12 写了个表情工具类EmojiUtil（未测试）
2014.07.16 写了个Random工具类用于取随机数、以及取Set、Map、List的随机一个对象（值）的方法
2014.07.18 Dialog加上alert的简单使用弹框功能
