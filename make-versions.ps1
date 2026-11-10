# 在專案根目錄執行：  powershell -ExecutionPolicy Bypass -File .\make-versions.ps1
# 依序建立 v0 到 v4 五個 commit 並打上 tag，之後 git push -u origin master --tags
$ErrorActionPreference = "Stop"
$src = "app\src\main\java\tw\edu\pu\csim\CHUNG\digitalink\MainActivity.kt"
$gradle = "app\build.gradle.kts"
$img = "app\src\main\res\drawable\minions.png"
$utf8 = New-Object System.Text.UTF8Encoding($false)

# 若已經有舊的 git 紀錄，詢問是否砍掉重做
if (Test-Path .git) {
  $a = Read-Host "已有 .git，要刪除重做嗎？(y/n)"
  if ($a -ne "y") { exit }
  Remove-Item .git -Recurse -Force
}
git init -b master

# v0 還不使用圖片，先移開，v1 再放回
if (Test-Path $img) { Move-Item $img "checkpoints\minions.png" -Force }

$steps = @(
  @{ tag="v0"; msg="v0：畫布基礎，繪製圓形與空心矩形";           gradle="checkpoints\build.v0.gradle.kts" },
  @{ tag="v1"; msg="v1：新增直線、圖片與路徑";                   gradle="checkpoints\build.v0.gradle.kts" },
  @{ tag="v2"; msg="v2：小小兵自動移動"; gradle="checkpoints\build.v0.gradle.kts" },
  @{ tag="v3"; msg="v3：拖曳塗鴉與開始/停止按鈕"; gradle="checkpoints\build.v0.gradle.kts" },
  @{ tag="v4"; msg="v4：超出邊界回到原點"; gradle="checkpoints\build.v0.gradle.kts" }
)
foreach ($s in $steps) {
  Copy-Item "checkpoints\$($s.tag)\MainActivity.kt" $src -Force
  Copy-Item $s.gradle $gradle -Force
  if ($s.tag -eq "v1") { Copy-Item "checkpoints\minions.png" $img -Force }
  git add -A
  # 用檔案傳訊息，避免 Windows PowerShell 中文變亂碼
  [System.IO.File]::WriteAllText("$PWD\.commitmsg", $s.msg, $utf8)
  git commit -F .commitmsg
  Remove-Item .commitmsg
  git tag -f $s.tag
}
Write-Host "完成。接著執行: git remote add origin <網址>; git push -u origin master --tags"
