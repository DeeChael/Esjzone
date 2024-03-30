# Esjzone

一个为安卓平台打造的 Esjzone 应用，基于 Jetpack Compose

查看 [NETWORK.md](./NETWORK.md) 来了解 Esjzone 网络请求相关内容

目前开发进度：首页的小说都能打开详细页并打开章节页面进行阅读了，~~接下来把搜索做出来就可以暂缓开发进度了。~~ \
搜索完成后会先完成收藏和列出所有小说的功能，然后就会完全放缓开发速度，因为已经可以使用了。 \
欢迎各位大佬提交 PR，我的开发能力有限，实在无法将这个软件做的十分完善。

### 提交错误信息需注意事项
1. 标明系统版本（要具体，格式举例为：HarmonyOS \[4.0.0.165(C00E155R4P16)] \[安卓版本信息]，再举一个例子：OriginOS 3 \[PD2055D_A_8.14.2] \[Android 13]）
2. 标明版本（如果是自行编译请提供 commit id 并且仅限于本仓库 commit 维护，如果为 fork 项目的问题将不予回复；如果为此仓库提供的编译版本，在“关于”界面可以查询到当前编译版本号）
3. 详细写出操作过程，比如：打开软件，点击搜索Tab，点击搜索框，输入“XXX”，搜索，下滑X本小说至“XXX”|下滑X次加载后，软件崩溃。
4. 如果能标明具体是哪本小说的问题，也请注明小说名称，说不定是小说的简介或其他数据有特例数据，因为有些特例数据难以采集

#### 更新于 2024.3.26
已完成搜索功能，已经处于一个可以正常使用的状态了

#### 更新于 2024.3.28
今天重新设计了个人界面，并为实现了分类界面，但是没做浏览界面，因为分类的小说是从论坛获取的，论坛获取只能获取到链接和名称，到时候还需要进行处理获取封面等操作

#### 更新于 2024.3.30
庆祝吧！现在除了主页几个最新小说等跳转到筛选界面、个人页面的收藏和历史记录以外所有可点击 UI 都已经可以使用了！此软件已经接近完整了！

#### 再次 更新于 2024.3.30
添加了小说列表界面，现在除了个人界面的收藏和历史记录以外，其他所有的地方都可以点了！软件所有功能均已实现！

#### 再再次 更新于 2024.3.30
小说阅读界面增加了底部栏（同顶部栏一样点击屏幕中央弹出），有跳转上一章、下一章的按钮，和调整阅读位置的滑块。 \ 
小说介绍页面增加了一个按钮可以 开始/继续阅读 \ 
小说介绍页的章节列表重新设计，阅读历史章节会高亮出来

## 吐槽
1. 密码明文储存，忘记密码会直接把你的密码发至邮箱
2. 前后端分离不完全，数据处理及其麻烦
3. 很逆天，为什么章节列表里还能塞文字，让本来就不好分析的数据雪上加霜。更操蛋的是，如果你想用论坛页面分析，有两个问题，一个是分组没了，二就是论坛页面的章节是乱序的。真是有够操蛋的，我现在仍然在考虑是否要把章节列表里的文字内容显示出来。
4. 没想到啊没想到，论坛的“下一章”按钮的超链接是自定义的，有的小说会点下一章会跳转到别的小说，我写跳转的时候发现章节对不上号去浏览器测试发现竟然不是同一本小说，离了大谱了！

## 开发计划
### 优化（优先级※※※※※）
1. 缓存处理一部分数据（如主页和个人界面）以避免多次、重复的加载导致的用户体验差
2. 重新设计 UI 使其符合 Material 规范（目前大部分内容都不符合）（大佬们救救，我的设计天赋很差，而且就算能想到好排版也不一定能做得出来，帮帮我！~~史瓦罗先生~~）
3. 考虑使用 Glide 替换 Coil 作图片库
4. 想办法使 isMinifyEnabled 选项可以开启（我尝试过，但是开启后安装后无法打开，会崩溃，需要调整 proguard 参数）以优化安装包大小
5. 考虑将软件变为两个版本：完整版、绿色版（不包括成人内容）

### 正常使用（优先级※※※※）
1. ~~搜索~~ 已完成（2024.3.26），支持 Endless Scrolling，无需手动切换分页 
2. ~~列出所有（不是指分类中的小说，指的是通过主页进入的可筛选的界面，对应的链接为如下格式：https://www.esjzone.me/list-01/ ）~~ 已完成（2024.3.30）
3. ~~分类~~
4. 收藏
5. ~~观看记录 （目前不知道应该用什么办法将软件的记录更新到 Esjzone 的后端）~~ 测试发现只要请求了论坛内容界面就会自动保存记录，所以不需要处理，现在需要做的部分为列出观看过的小说
6. 评论
7. 论坛功能
8. \[非必须] 将论坛的发帖页面和整合页读取到的章节分开显示，但是论坛里是乱序的，可能是因为编辑者有时会修改前文内容，并且论坛是按时间排序的 

### 功能拓展（优先级※※）
1. 本地收藏
2. 下载
3. 注册 （由于现在想要一个账号可以正常使用需要在注册后到水贴发一条评论，所以我在考虑是否要把注册内置在软件中，但我建议是到网站上注册后再来使用软件）
4. 文章内容、小说名称、作者、章节名等的 简/繁体 切换

## 演示（截图 & 视频）

<table>
  <tr>
    <td>登录页面</td>
    <td>登录页面（尝试登录）</td>
    <td>小说详细页（已过时）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/1.jpg" alt="1"></td>
    <td><img src="./screenshots/showcase/2.jpg" alt="2"></td>
    <td><img src="./screenshots/showcase/4.jpg" alt="3"></td>
  </tr> 
  <tr>
    <td>章节列表（普通章节）</td>
    <td>章节列表（分组-未展开）</td>
    <td>章节列表（分组-已展开）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/5.jpg" alt="5"></td>
    <td><img src="./screenshots/showcase/6.jpg" alt="6"></td>
    <td><img src="./screenshots/showcase/7.jpg" alt="7"></td>
  </tr> 
  <tr>
    <td>章节阅读界面</td>
    <td>章节阅读界面（显示顶栏）</td>
    <td>章节阅读界面（带有图片的展示）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/8.jpg" alt="8"></td>
    <td><img src="./screenshots/showcase/9.jpg" alt="9"></td>
    <td><img src="./screenshots/showcase/10.jpg" alt="10"></td>
  </tr> 
  <tr>
    <td>小说详细页（带有图片的简介-已过时）</td>
    <td>搜索页面（未输入-已过时）</td>
    <td>搜索页面（正在输入-已过时）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/11.jpg" alt="11"></td>
    <td><img src="./screenshots/showcase/12.jpg" alt="12"></td>
    <td><img src="./screenshots/showcase/13.jpg" alt="13"></td>
  </tr> 
  <tr>
    <td>搜索结果</td>
    <td>小说详细页（更新后-已过时）</td>
    <td>搜索页面（未输入-带有搜索历史）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/14.jpg" alt="14"></td>
    <td><img src="./screenshots/showcase/15.jpg" alt="15"></td>
    <td><img src="./screenshots/showcase/16.jpg" alt="16"></td>
  </tr> 
  <tr>
    <td>个人主页（施工中，仅为测试用）</td>
    <td>个人主页（设计定稿）</td>
    <td>设置界面</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/17.jpg" alt="17"></td>
    <td><img src="./screenshots/showcase/18.jpg" alt="18"></td>
    <td><img src="./screenshots/showcase/19.jpg" alt="19"></td>
  </tr> 
  <tr>
    <td>主页（关闭成人内容）</td>
    <td>分类界面</td>
    <td>小说列表界面</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/20.jpg" alt="20"></td>
    <td><img src="./screenshots/showcase/21.jpg" alt="21"></td>
    <td><img src="./screenshots/showcase/22.jpg" alt="22"></td>
  </tr> 
  <tr>
    <td>新设置界面</td>
    <td>关于 界面</td>
    <td>底部栏展示（无上一章）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/23.jpg" alt="23"></td>
    <td><img src="./screenshots/showcase/24.jpg" alt="24"></td>
    <td><img src="./screenshots/showcase/25.jpg" alt="25"></td>
  </tr> 
  <tr>
    <td>底部栏展示（有上下章）</td>
    <td>作品简介界面（最新）</td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/26.jpg" alt="26"></td>
    <td><img src="./screenshots/showcase/27.jpg" alt="27"></td>
  </tr> 
</table>

软件主页

https://github.com/DeeChael/DeeChael/assets/63186003/bc835c1c-2206-4788-8c2e-70c10a173e25