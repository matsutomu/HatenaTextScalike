# 初心者向け ScalikeJDBC サンプル

## はじめに
・Github 上で公開されている以下のコンテンツを参考にして作成しました。
 [Scala によるデータベースプログラミング (はてな Textbook)](https://github.com/hatena/Hatena-Textbook/blob/master/database-programming-scala.md)

・Slick 3 が難しかったのでScalikeJDBCを勉強したかった。

・Scala の文法が少し理解できはじめて、JDBC詳しくないけどDBにアクセスするプログラムを作成してみたかった。

コンテンツを参考にして、Slickの部分をScalikeJDBCにて置き換えてみました。

## 使っているもの

・Java 8
・Scala 2.11
・sbt 0.13.x
 [はじめるsbt](http://www.scala-sbt.org/0.13/docs/ja/)

・[H2](http://www.h2database.com/html/main.html) データベース
 ローカルPCで簡単に実行できるデータベースエンジンです。（アプリに組み込む事もできるし、サーバーとしても利用可）

・[flyway](https://flywaydb.org/)
 DBマイグレーションツール。テーブルの構造変更や、初期データの投入とかを管理するためのツール。

・[specs2](https://etorreborre.github.io/specs2/)
 Scala 用のテストフレームワーク。

・ScalikeJDBC 2.3.x
 Scala 用のデータベースアクセス用 フレームワーク。

## システム構成
・src/scala/model
 論理層間の値をやりとりするための"case class"を定義した。

・src/scala/repository
 データベースアクセス用のクラスを格納。更にdaoフォルダを用意して、ScalikeJDBCからの自動生成クラスを格納した。

・src/scala/service
 ひとまとまりにした処理を定義。ここからデータベース操作のrepositoryを呼び出す。

・src/scala/cli
 コンソールでの各種処理実行を定義した。

今後はWebフレームワークも組み込んでみたいです。


<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/"><img alt="クリエイティブ・コモンズ・ライセンス" style="border-width:0" src="http://i.creativecommons.org/l/by-nc-sa/2.1/jp/88x31.png" /></a><br />この 作品 は <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/2.1/jp/">クリエイティブ・コモンズ 表示 - 非営利 - 継承 2.1 日本 ライセンスの下に提供されています。</a>



