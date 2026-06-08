# Renderへのデプロイ手順

このドキュメントでは、itoゲームアプリケーションをRenderにデプロイする手順を説明します。

## 前提条件

- GitHubアカウント
- Renderアカウント（無料で作成可能）

## 手順

### 1. GitHubリポジトリの作成

1. [GitHub](https://github.com)にログイン
2. 右上の「+」→「New repository」をクリック
3. リポジトリ名を入力（例：`ito-game`）
4. 「Public」を選択（無料プランの場合）
5. 「Create repository」をクリック

### 2. コードをGitHubにプッシュ

プロジェクトディレクトリで以下のコマンドを実行：

```bash
cd "C:\pleiades\2025-12\workspace\ito-spring.zip_expanded\ito-spring"

# Gitリポジトリを初期化
git init

# すべてのファイルを追加
git add .

# コミット
git commit -m "Initial commit for Render deployment"

# GitHubリポジトリをリモートとして追加（URLは自分のリポジトリに置き換える）
git remote add origin https://github.com/YOUR_USERNAME/ito-game.git

# メインブランチにプッシュ
git branch -M main
git push -u origin main
```

### 3. Renderでのデプロイ設定

1. [Render](https://render.com)にアクセスしてサインアップ/ログイン
2. ダッシュボードで「New +」→「Web Service」をクリック
3. 「Connect a repository」でGitHubを選択
4. 作成したリポジトリ（`ito-game`）を選択
5. 以下の設定を確認/入力：
   - **Name**: `ito-game`（任意の名前）
   - **Region**: `Singapore`（日本に近いリージョン）
   - **Branch**: `main`
   - **Root Directory**: 空欄のまま
   - **Runtime**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/ito-game-1.0.0.jar`
6. 「Free」プランを選択
7. 「Create Web Service」をクリック

### 4. デプロイの確認

- デプロイには5〜10分程度かかります
- ダッシュボードでデプロイの進行状況を確認できます
- デプロイが完了すると、URLが表示されます（例：`https://ito-game-xxxx.onrender.com`）

### 5. アプリケーションの使い方

デプロイが完了したら：

1. **進行役**は `https://your-app-name.onrender.com/admin` にアクセス
2. ルームを選択してラウンドを開始
3. **admin画面に表示される「参加者に共有するURL」をコピー**
4. そのURLをTeamsやSlackなどで参加者に共有
5. 参加者はそのURLから自分の数字を引く

**重要**: admin画面に表示されるURLは自動的に公開URLになります。ローカルのlocalhostではありません。

## 注意事項

### 無料プランの制限

- 15分間アクセスがないとスリープ状態になります
- スリープ状態から復帰には30秒〜1分程度かかります
- 月750時間まで無料で利用可能
- 初回アクセス時は起動に時間がかかる場合があります

### カスタムドメインの設定（オプション）

有料プランにアップグレードすると、独自ドメインを設定できます。

## トラブルシューティング

### ビルドエラーが発生する場合

1. Renderのログを確認
2. ローカルで `mvn clean package` が成功することを確認
3. Java 17が使用されていることを確認

### アプリケーションが起動しない場合

1. Renderのログで起動エラーを確認
2. `application.properties`の設定を確認
3. ポート設定が正しいか確認（`${PORT:8080}`）

### 共有URLにアクセスできない場合

1. Renderのダッシュボードでサービスが起動していることを確認
2. admin画面に表示されているURLが正しいか確認
3. スリープ状態の場合は、少し待ってから再度アクセス

## 更新方法

コードを更新してGitHubにプッシュすると、Renderが自動的に再デプロイします：

```bash
git add .
git commit -m "Update description"
git push
```

## サポート

問題が発生した場合は、[Renderのドキュメント](https://render.com/docs)を参照してください。