# 初心者向け ScalikeJDBC サンプル

## はじめに
Github 上で公開されている以下のコンテンツを参考にして作成しました。  
[Scala によるデータベースプログラミング (はてな Textbook)](https://github.com/hatena/Hatena-Textbook/blob/master/database-programming-scala.md)

Slick 3 が難しかったので、ScalikeJDBCの学習を兼ねつつSlickの部分をScalikeJDBCに置き換えてみました。

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
・src/main/scala/model - 論理層間の値をやりとりするための"case class"を定義。
・src/main/scala/repository - データベースアクセス用のクラスを格納。更にdaoフォルダを用意して、ScalikeJDBCからの自動生成クラスを格納。
・src/main/scala/service - ひとまとまりにした処理を定義。ここからデータベース操作のrepositoryを呼び出す。
・src/main/scala/cli - コンソールでの各種処理実行を定義。
・src/main/scala/controller - Webのリクエストの窓口。
・src/main/scala/templates - Webのtemplate engine (scalate) 関係。
・src/main/scala/util - 他で使う共通処理。
・src/webapp/  - Web関係のファイル。


## Skinny Framework 対応

./skinny run で ローカル環境で実行可能なWebアプリケーションです。
サンプル的なアプリとしてご参照ください。

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/"><img alt="クリエイティブ・コモンズ・ライセンス" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/2.1/jp/88x31.png" /></a><br />この 作品 は <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/">クリエイティブ・コモンズ 表示 - 非営利 - 継承 2.1 日本 ライセンスの下に提供されています。</a>



