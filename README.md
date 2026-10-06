# 顧客管理システム

Spring Bootを使用して作成した顧客管理Webアプリです。

## 概要

顧客情報の登録・検索・更新・削除（CRUD）ができるアプリケーションです。

ユーザー登録機能とログイン機能を実装し、認証後に顧客情報を管理できます。

## 主な機能

### ユーザー機能

- ユーザー登録
- ログイン
- マイページ
- 情報照会
- メールアドレス変更
- パスワード変更
- 退会

### 顧客管理機能

- 顧客登録
- 顧客一覧表示
- 顧客検索
- 顧客情報更新
- 顧客パスワード変更
- 顧客削除

## 使用技術

- Java 17
- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- PostgreSQL
- HTML
- CSS
- Git / GitHub
- Render

## 画面一覧

- ログイン画面
- 新規登録画面
- マイページ
- 顧客一覧画面
- 顧客検索画面
- 顧客登録画面
- エラー画面

## 工夫した点

- 共通CSSを作成し、全画面でデザインを統一
- PostogerSQLを利用してデータを永続化
- レスポンシブを意識したレイアウト
- Renderへデプロイし、外部から利用可能な環境を構築
- ユーザー機能と顧客管理機能を分離して実装

## URL

### ユーザー側

https://customer-management-app-vltk.onrender.com/user

### 管理側

https://customer-management-app-vltk.onrender.com/customer

## 今後の改善予定

- バリデーション強化
- 権限管理機能
- デザイン改善
- ページネーション
- 検索機能拡張
