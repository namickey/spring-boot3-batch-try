# spring-boot3-batch-try

Spring-Bootでバッチアプリケーション開発

## やりたいこと
* Spring-Bootを使った効率的な開発を行い、ワンコマンドでバッチアプリケーションを起動する
* JAVAだけがインストールされたマシンで、アプリケーションを起動する
* ORマッパーは`MyBatis`を使う
* setter,getterは実装せず、`lombok`で生成する
* アプリケーション開発は、`Spring Initializr`からデモアプリケーションをダウンロードして始める
* `Maven`を使ったライブラリ管理（自動でjarをダウンロードして、クラスパスを通してくれる）

> [!TIP]
> 【Java】Lombokで冗長コードを削減しよう  
> https://www.casleyconsulting.co.jp/blog/engineer/107/ 

## Spring-Bootとは

- 設定ファイルレス  
  Spring-WEB-MVCに対して、あらかじめ様々な設定が設定済みとなっているため、自分で設定ファイルを書く量が少ない

- 簡単起動  
  組み込みH2データベースを使用するため、データベースのインストールが不要

- 素早く開始  
  `Spring Initializr`を使って必要なライブラリが組み込まれた初期構成のアプリケーションをダウンロードできる

- 短所としては、ライフサイクルが比較的短い  
  https://spring.pleiades.io/projects/spring-boot#support

## 前提環境

以下がインストール済みであること
* JDK 17
* git

## 使用するフレームワーク

* spring-boot3.4
* spring-boot-starter-batch
* mybatis
* lombok
* 組み込みh2データベース
* 組み込みMaven 3

> [!TIP]
> Java環境構築(Windows版)　JDKインストール  
> https://www.techfun.co.jp/services/magazine/java/windows-jdk-install.html  
> 
> Java環境構築(Windows版)　パスの設定  
> https://www.techfun.co.jp/services/magazine/java/windows-jdk-pathset.html  
> 
> Git Bashって使ってる？Windowsで動く意外にすごい便利ツール  
> https://www.sejuku.net/blog/72673  

## ディレクトリ階層

今回、作成するアプリケーションのディレクトリ階層
```
C:.
│  .gitattributes
│  .gitignore
│  initializr.png
│  mvnw
│  mvnw.cmd
│  pom.xml
│  README.md
├─.mvn
│  └─wrapper
│          maven-wrapper.properties
├─h2db
│      .gitkeep
│      h2-2.3.232.jar
│      testdb.mv.db
│      testdb.trace.db
├─input-data
│      user.csv
├─output-data
│      .gitkeep
│      users.csv
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─example
│  │  │          └─demo
│  │  │              │  DemoApplication.java
│  │  │              │
│  │  │              ├─batch
│  │  │              │  └─master
│  │  │              │      └─user
│  │  │              │          ├─receive
│  │  │              │          │      ImportUsersConfig.java
│  │  │              │          │      ImportUsersItem.java
│  │  │              │          │      ImportUsersProcessor.java
│  │  │              │          │      ImportUsersWriter.java
│  │  │              │          │
│  │  │              │          └─send
│  │  │              │                  ExportUsersConfig.java
│  │  │              │                  ExportUsersFieldExtractor.java
│  │  │              │                  ExportUsersItem.java
│  │  │              │                  ExportUsersProcessor.java
│  │  │              │
│  │  │              ├─common
│  │  │              │  ├─entity
│  │  │              │  │      Users.java
│  │  │              │  │
│  │  │              │  └─mapper
│  │  │              │          UsersMapper.java
│  │  │              │
│  │  │              └─core
│  │  │                  ├─config
│  │  │                  │      BatchConfiguration.java
│  │  │                  │      BatchExitCodeGenerator.java
│  │  │                  │
│  │  │                  ├─exception
│  │  │                  │      AppException.java
│  │  │                  │      BatchSkipPolicy.java
│  │  │                  │
│  │  │                  └─listener
│  │  │                          BatchChunkListener.java
│  │  │                          JobListener.java
│  │  │
│  │  └─resources
│  │          application.properties
│  │          schema-all.sql
```

## アプリケーションの構造

![アプリケーションの構造](app.png)
* Readerで、1行ずつデータを読み込み、Processerに渡す。
* Processerでは、1行ずつデータ加工を行う。ここでは入力データや出力データにはアクセスしない。
* Writerでは、複数行を受け取り、一括で書き込みを行う。
* Configは上記を取りまとめる設定クラス。ジョブやステップの制御も行う。

## 準備 githubからソースコードを取得

gitを使ってソースコードをダウンロードする
```
コマンドプロンプトで実行
git clone https://github.com/namickey/spring-boot3-batch-try.git
cd spring-boot3-batch-try
```

## 実行 spring-boot:run

実行する
```shell
コマンドプロンプトで実行
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.name=importUsersJob"
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.name=exportUsersJob"
```

## H2DBの確認
```shell
H2DBサーバの起動
java -jar h2db/h2-2.3.232.jar

DB接続
http://localhost:8082/login.jsp

保存済設定：Generic H2(Server)
JDBC URL：jdbc:h2:./h2db/testdb;AUTO_SERVER=TRUE
ユーザ名：sa
パスワード：
```

## やってみよう 

1. `Spring Initializr`から初期構成のアプリケーションをダウンロードする  
https://start.spring.io/
![initializr](initializr.png)

2. 統合開発環境を使って、今動かしたソースコードと同じものを実装し、動作確認する
> [!TIP]
> 統合開発環境（vscode、eclipse、intelliJ）を使おう  
>   * vscode：おススメ、最新、軽量  
>   * eclipse：古き重き友人  
>   * intelliJ：おススメだが、WEB開発時の`spring-boot-devtools`の自動デプロイ機能と相性が悪い。intelliJは入力する度にファイル保存されてしまうから。  

3. 自分のgithubアカウントを作って、作ったソースを公開しよう

4. GitHubCopilotを使ってみよう

> [!TIP]
> VS CodeでGitHub Copilot 無料版の導入　備忘録  
> https://zenn.dev/yuta_haruna/articles/fb809e68e6bae5  

