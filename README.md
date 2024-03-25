# Esjzone

一个为安卓平台打造的 Esjzone 应用，基于 Jetpack Compose

查看 [NETWORK.md](./NETWORK.md) 来了解 Esjzone 网络请求相关内容

目前开发进度：首页的小说都能打开详细页并打开章节页面进行阅读了，接下来把搜索做出来就可以暂缓开发进度了。 \
搜索完成后会先完成收藏和列出所有小说的功能，然后就会完全放缓开发速度，因为已经可以使用了。 \
欢迎各位大佬提交 PR，我的开发能力有限，实在无法将这个软件做的十分完善。

## 吐槽

1. 密码明文储存，忘记密码会直接把你的密码发至邮箱
2. 前后端分离不完全，数据处理及其麻烦
3.
很逆天，为什么章节列表里还能塞文字，让本来就不好分析的数据雪上加霜。更操蛋的是，如果你想用论坛页面分析，有两个问题，一个是分组没了，二就是论坛页面的章节是乱序的。真是有够操蛋的，我现在仍然在考虑是否要把章节列表里的文字内容显示出来。

## 开发计划
### 正常使用
1. 搜索
2. 列出所有
3. 分类
4. 收藏
5. 观看记录 （目前不知道应该用什么办法将软件的记录更新到 Esjzone 的后端）
6. 评论
7. 论坛功能
8. \[非必须] 将论坛的发帖页面和整合页读取到的章节分开显示，但是论坛里是乱序的，可能是因为编辑者有时会修改前文内容，并且论坛是按时间排序的 

### 功能拓展
1. 本地收藏
2. 下载
3. 注册 （由于现在想要一个账号可以正常使用需要在注册后到水贴发一条评论，所以我在考虑是否要把注册内置在软件中，但我建议是到网站上注册后再来使用软件）
4. 重新设计 UI （大佬们救救，我的设计天赋很差，而且就算能想到好排版也不一定能做得出来，帮帮我！~~史瓦罗先生~~）

## 演示（截图 & 视频）

<table>
  <tr>
    <td><img src="./screenshots/showcase/1.jpg" alt="1" width=360px height=640px ></td>
    <td><img src="./screenshots/showcase/2.jpg" alt="2" width=360px height=640px></td>
    <td><img src="./screenshots/showcase/4.jpg" alt="2" width=360px height=640px></td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/5.jpg" alt="1" width=360px height=640px ></td>
    <td><img src="./screenshots/showcase/6.jpg" alt="2" width=360px height=640px></td>
    <td><img src="./screenshots/showcase/7.jpg" alt="2" width=360px height=640px></td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/8.jpg" alt="1" width=360px height=640px ></td>
    <td><img src="./screenshots/showcase/9.jpg" alt="2" width=360px height=640px></td>
    <td><img src="./screenshots/showcase/10.jpg" alt="2" width=360px height=640px></td>
  </tr> 
  <tr>
    <td><img src="./screenshots/showcase/11.jpg" alt="1" width=360px height=640px ></td>
  </tr> 
</table>

https://github.com/DeeChael/DeeChael/assets/63186003/bc835c1c-2206-4788-8c2e-70c10a173e25