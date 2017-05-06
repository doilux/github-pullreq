# github-pullreq

## 概要
PullRequestがマージされるまでの時間を集計してレポートする。

## バージョンなど
Scala:2.12.2
sbt:0.13.15
jdk:1.8.0_131

## 使い方
src/main/resources/application.conf.sampleをsrc/main/resources/application.confにリネームして、githubのページから取得したtokenを設定した後、以下を実行する。

```
sbt compile:run
```


