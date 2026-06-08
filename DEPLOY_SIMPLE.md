# 簡単デプロイ手順（個人GitHubアカウント使用）

## ステップ1: 個人のGitHubアカウントを作成（5分）

1. https://github.com/signup にアクセス
2. 個人のメールアドレスを入力（会社のメールでなくてOK）
3. パスワードを設定
4. ユーザー名を決定
5. メール認証を完了

## ステップ2: GitHubリポジトリを作成（2分）

1. GitHubにログイン
2. 右上の「+」→「New repository」をクリック
3. Repository name: `ito-game`
4. **Private**を選択（公開したくない場合）
5. 「Create repository」をクリック
6. 表示されるURLをコピー（例：`https://github.com/YOUR_USERNAME/ito-game.git`）

## ステップ3: コードをプッシュ（3分）

PowerShellで以下を実行：

```powershell
cd "C:\pleiades\2025-12\workspace\ito-spring.zip_expanded\ito-spring"

# リモートリポジトリを追加（URLは自分のものに置き換える）
git remote add origin https://github.com/YOUR_USERNAME/ito-game.git

# プッシュ
git push -u origin main
```

初回プッシュ時、GitHubの認証が求められます：
- ユーザー名：GitHubのユーザー名
- パスワード：**Personal Access Token**（下記参照）

### Personal Access Tokenの作成

パスワードの代わりにトークンが必要です：

1. GitHub → 右上のアイコン → Settings
2. 左下の「Developer settings」
3. 「Personal access tokens」→「Tokens (classic)」
4. 「Generate new token」→「Generate new token (classic)」
5. Note: `ito-game-deploy`
6. Expiration: `90 days`
7. **repo**にチェック
8. 「Generate token」をクリック
9. 表示されたトークンをコピー（後で見れないので注意！）

このトークンをパスワードとして使用してください。

## ステップ4: Renderでデプロイ（5分）

1. https://render.com にアクセス
2. 「Get Started」→ GitHubでサインアップ
3. 「New +」→「Web Service」
4. 「Connect a repository」→ GitHubを選択
5. 作成した`ito-game`リポジトリを選択
6. 設定を確認：
   - Name: `ito-game`
   - Region: `Singapore`
   - Branch: `main`
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/ito-game-1.0.0.jar`
7. **Free**プランを選択
8. 「Create Web Service」をクリック

## ステップ5: デプロイ完了を待つ（5-10分）

- Renderのダッシュボードでビルドログを確認
- 「Live」と表示されたら完了
- URLが表示されます（例：`https://ito-game-xxxx.onrender.com`）

## ステップ6: 使い方

1. **進行役**：`https://your-app.onrender.com/admin` にアクセス
2. ルームを選択してラウンド開始
3. **画面に表示される「参加者に共有するURL」をコピー**
4. TeamsやSlackで参加者に共有
5. 参加者はそのURLから数字を引く

## 重要な注意点

### 無料プランの制限
- 15分間アクセスがないとスリープ
- スリープから復帰に30秒〜1分かかる
- 月750時間まで無料

### 初回アクセス時
- 起動に時間がかかる場合があります
- 進行役は少し早めにアクセスしておくと良い

### 「mvn: command not found」エラーが出る場合

Renderの設定を以下に変更してください：

1. Renderのダッシュボードで該当サービスを選択
2. 「Settings」タブをクリック
3. 「Build & Deploy」セクションで：
   - **Runtime**: `Docker`に変更
   - **Dockerfile Path**: `./Dockerfile`
4. 「Save Changes」をクリック
5. 「Manual Deploy」→「Deploy latest commit」で再デプロイ

Dockerを使うことで、Maven環境が確実にセットアップされます。

## コード更新時

コードを変更した場合：

```powershell
cd "C:\pleiades\2025-12\workspace\ito-spring.zip_expanded\ito-spring"
git add .
git commit -m "Update code"
git push
```

Renderが自動的に再デプロイします。

## トラブルシューティング

### プッシュ時に認証エラー
→ Personal Access Tokenを正しく入力しているか確認

### ビルドエラー
→ Renderのログを確認。ローカルで`mvn clean package`が成功するか確認

### アプリが起動しない
→ Renderのログで「Started ItoApplication」が表示されているか確認

## 完了！

これで誰でもアクセスできるitoゲームが完成しました🎉