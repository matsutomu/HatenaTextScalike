# 初心者向け ScalikeJDBC / Skinny Framework サンプル

## はじめに
Github 上で公開されている以下のコンテンツを参考に作成しました。
[Scala によるデータベースプログラミング (はてな Textbook)](https://github.com/hatena/Hatena-Textbook/blob/master/database-programming-scala.md)

Slick 3 が難しかったので、ScalikeJDBCの学習を兼ねつつSlickの部分をScalikeJDBCに置き換えてみました。

また、後からSkinny Frameworkを使ってWebアプリ化にも挑戦しました。


## 使っているもの

・Java 8

・Scala 2.12

・sbt 0.13.x  
 [はじめるsbt](http://www.scala-sbt.org/0.13/docs/ja/)

・[H2](http://www.h2database.com/html/main.html) データベース  
 ローカルPCで簡単に実行できるデータベースエンジンです。（アプリに組み込む事もできるし、サーバーとしても利用可）  

・[flyway](https://flywaydb.org/)  
 DBマイグレーションツール。テーブルの構造変更や、初期データの投入とかを管理するためのツール。  

・[specs2](https://etorreborre.github.io/specs2/)  
 Scala 用のテストフレームワーク。  

・[ScalikeJDBC 2.4.x](http://scalikejdbc.org/)
 Scala 用のデータベースアクセス用 フレームワーク。  

・[Skinny Framework 2.3.2](http://skinny-framework.org/)
 Scala 用のフルスタックのWebアプリケーションフレームワーク。


## システム構成

|パス|説明|
|src/main/resources|設定ファイル色々。|
|src/main/scala/repository|データベースアクセス用のクラスを格納。更にdaoフォルダを用意して、ScalikeJDBCからの自動生成クラスを格納。|
|src/main/scala/model|論理層間の値をやりとりするための"case class"を定義。|
|src/main/scala/repository|データベースアクセス用のクラスを格納。更にdaoフォルダを用意して、ScalikeJDBCからの自動生成クラスを格納。|
|src/main/scala/service|ひとまとまりにした処理を定義。ここからデータベース操作のrepositoryを呼び出す。|
|src/main/scala/cli|コンソールでの各種処理実行を定義。|
|src/main/scala/controller|Webのリクエストの窓口。|
|src/main/scala/templates|Webのtemplate engine (scalate) 関係。|
|src/main/scala/util|他で使う共通処理。|
|src/webapp/|Web関係のファイル。|


```

HatenaTextScalike/
├── README.md
├── _skinny_assembly_settings.sbt
├── bin
│   └── sbt-launch.jar
├── build.sbt
├── build.sbt~
├── db
├── project
│   ├── _skinny_assembly.sbt
│   ├── build.properties
│   ├── plugins.sbt
│   ├── plugins.sbt~
│   └── scalikejdbc.properties
├── sbt
├── sbt-debug
├── sbt-debug.bat
├── sbt.bat
├── skinny
├── skinny.bat
├── src
│   ├── main
│   │   ├── resources
│   │   │   ├── application.conf
│   │   │   ├── db
│   │   │   │   └── migration
│   │   │   │       ├── V1__create_tables.sql
│   │   │   │       └── V2__alter_tables.sql
│   │   │   ├── logback.xml
│   │   │   ├── logback.xml.simple
│   │   │   ├── messages.conf
│   │   │   └── messages_ja.conf
│   │   ├── scala
│   │   │   ├── Bootstrap.scala
│   │   │   ├── lib
│   │   │   ├── simple
│   │   │   │   └── bookmark
│   │   │   │       ├── cli
│   │   │   │       │   ├── BookmarkCLI.scala
│   │   │   │       │   └── BookmarkCLI.scala~
│   │   │   │       ├── controller
│   │   │   │       │   ├── ApiController.scala
│   │   │   │       │   ├── ApplicationController.scala
│   │   │   │       │   ├── BookmarksController.scala
│   │   │   │       │   ├── Controllers.scala
│   │   │   │       │   ├── MsUsersController.scala
│   │   │   │       │   └── RootController.scala
│   │   │   │       ├── model
│   │   │   │       │   ├── Bookmark.scala
│   │   │   │       │   ├── Entry.scala
│   │   │   │       │   └── User.scala
│   │   │   │       ├── repository
│   │   │   │       │   ├── Bookmarks.scala
│   │   │   │       │   ├── Entries.scala
│   │   │   │       │   ├── Identifier.scala
│   │   │   │       │   ├── MsUserDefault.scala
│   │   │   │       │   ├── Users.scala
│   │   │   │       │   └── dao
│   │   │   │       │       ├── MsUser.scala
│   │   │   │       │       ├── SchemaVersion.scala
│   │   │   │       │       ├── TrBookmark.scala
│   │   │   │       │       └── TrEntry.scala
│   │   │   │       ├── service
│   │   │   │       │   ├── BookmarkApp.scala
│   │   │   │       │   ├── Error.scala
│   │   │   │       │   └── UserApp.scala
│   │   │   │       ├── templates
│   │   │   │       │   └── ScalatePackage.scala
│   │   │   │       └── util
│   │   │   │           ├── Authenticate.scala
│   │   │   │           └── MyTimestampsFeature.scala
│   │   │   └── worker
│   │   │       └── package.scala
│   │   └── webapp
│   │       ├── WEB-INF
│   │       │   ├── assets
│   │       │   │   ├── build.sbt
│   │       │   │   ├── coffee
│   │       │   │   ├── jsx
│   │       │   │   ├── less
│   │       │   │   ├── scala
│   │       │   │   └── scss
│   │       │   ├── layouts
│   │       │   │   ├── default.html
│   │       │   │   ├── default.ssp
│   │       │   │   └── default_bookmarks.ssp
│   │       │   ├── views
│   │       │   │   ├── bookmarks
│   │       │   │   │   ├── _form.html.ssp
│   │       │   │   │   ├── add.html.ssp
│   │       │   │   │   ├── edit.html.ssp
│   │       │   │   │   ├── index.html.ssp
│   │       │   │   │   ├── new.html.ssp
│   │       │   │   │   └── show.html.ssp
│   │       │   │   ├── bookmarks.html.ssp
│   │       │   │   ├── error
│   │       │   │   │   ├── 403.html
│   │       │   │   │   ├── 403.html.jade
│   │       │   │   │   ├── 403.html.mustache
│   │       │   │   │   ├── 403.html.scaml
│   │       │   │   │   ├── 403.html.ssp
│   │       │   │   │   ├── 404.html
│   │       │   │   │   ├── 404.html.jade
│   │       │   │   │   ├── 404.html.mustache
│   │       │   │   │   ├── 404.html.scaml
│   │       │   │   │   ├── 404.html.ssp
│   │       │   │   │   ├── 406.html
│   │       │   │   │   ├── 406.html.jade
│   │       │   │   │   ├── 406.html.mustache
│   │       │   │   │   ├── 406.html.scaml
│   │       │   │   │   ├── 406.html.ssp
│   │       │   │   │   ├── 500.html
│   │       │   │   │   ├── 500.html.jade
│   │       │   │   │   ├── 500.html.mustache
│   │       │   │   │   ├── 500.html.scaml
│   │       │   │   │   ├── 500.html.ssp
│   │       │   │   │   ├── 503.html
│   │       │   │   │   ├── 503.html.jade
│   │       │   │   │   ├── 503.html.mustache
│   │       │   │   │   ├── 503.html.scaml
│   │       │   │   │   └── 503.html.ssp
│   │       │   │   ├── msusers
│   │       │   │   │   ├── _form.html.ssp
│   │       │   │   │   ├── edit.html.ssp
│   │       │   │   │   ├── index.html.ssp
│   │       │   │   │   ├── new.html.ssp
│   │       │   │   │   └── show.html.ssp
│   │       │   │   └── root
│   │       │   │       └── index.html.ssp
│   │       │   └── web.xml
│   │       ├── assets
│   │       │   ├── css
│   │       │   │   └── default.css
│   │       │   └── js
│   │       └── favicon.ico
│   └── test
│       ├── resources
│       │   └── factories.conf
│       └── scala
│           ├── service
│           │   └── BookmarkAppSpec.scala
│           ├── settings
│           │   └── DBSettings.scala
│           └── simple
│               └── bookmark
│                   └── repository
│                       ├── BookmarksSpec.scala
│                       ├── EntriesSpec.scala
│                       ├── IdentifierSpec.scala
│                       ├── UsersSpec.scala
│                       └── dao
│                           ├── MsUserSpec.scala
│                           ├── TrBookmarkSpec.scala
│                           └── TrEntrySpec.scala
└── task
    └── src
        └── main
            └── scala
                └── TaskRunner.scala

```

## Skinny Framework 対応

./skinny run で ローカル環境で実行可能なWebアプリケーションです。
サンプル的なアプリとしてご参照ください。

## 実行の仕方

```bash
$git clone git@github.com:matsutomu/HatenaTextScalike.git

$./skinny db:migrate
・・・・
[success]
$./skinny test
・・・
[success] Total time: 69 s, completed 2017/02/03 0:22:07
$./skinny run
・・・
[success] Total time: 8 s, completed 2017/02/03 0:18:28
1. Waiting for source changes... (press enter to interrupt)

```

test 実行でユーザーをデータベースにテスト用の各種情報を設定しています。
ブラウザを立ち上げて、http://localhost:8080/ を表示すると、ログイン画面が出てきます。
ログインIDはAlice でパスワードは1234でログイン出来ます。



<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/"><img alt="クリエイティブ・コモンズ・ライセンス" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/2.1/jp/88x31.png" /></a><br />この 作品 は <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/">クリエイティブ・コモンズ 表示 - 非営利 - 継承 2.1 日本 ライセンスの下に提供されています。</a>



