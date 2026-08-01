# YT Client (legitimate YouTube Data API app)

A small Android app that browses/searches YouTube (official Data API v3), plays videos through
YouTube's own official embeddable player, and has a "Music Mode" that shrinks the video and shows
an audio-player-style UI while playback continues.

**Important:** because playback uses YouTube's official embedded player (required by YouTube's
Terms of Service), ads will still show during videos — this app does not and cannot strip them.
What you get instead: a custom UI, no bloat/cache creep, and a music-focused mode.

---

## Step 1 — Get a free YouTube Data API key

1. Go to https://console.cloud.google.com/
2. Create a new project (top left, "Select a project" → "New Project"). Any name is fine.
3. In the search bar, search for **"YouTube Data API v3"** and click **Enable**.
4. Go to **APIs & Services → Credentials → Create Credentials → API key**.
5. Copy the key it gives you (looks like `AIzaSy...`). Keep this private — don't paste it into
   any public file or commit it directly to your repo.
6. Optional but recommended: click "Restrict key" → under **API restrictions**, choose
   "Restrict key" and select only **YouTube Data API v3**.

The free tier gives you 10,000 quota units/day. Each search costs 100 units (~100 searches/day),
which is plenty for personal use.

## Step 2 — Create your GitHub repository

1. Go to https://github.com/new, name it (e.g. `yt-client`), keep it **Private** if you prefer,
   click **Create repository**.
2. On the empty repo page, click **"uploading an existing file"** (or "Add file → Upload files").
3. Extract the zip I've given you, then **drag the entire contents of the `YTClient` folder**
   (not the folder itself — its contents: `app/`, `.github/`, `build.gradle.kts`, etc.) into the
   GitHub upload box. Modern GitHub's uploader preserves folder structure, including the hidden
   `.github` folder.
4. Commit directly to `main`.

## Step 3 — Add your API key as a GitHub secret

Never put the raw key in a file in the repo. Instead:

1. In your repo, go to **Settings → Secrets and variables → Actions**.
2. Click **New repository secret**.
3. Name: `YOUTUBE_API_KEY`
4. Value: paste the key from Step 1.
5. Click **Add secret**.

## Step 4 — Run the build

1. Go to the **Actions** tab of your repo.
2. You should see a workflow called **"Build APK"**. If it hasn't run automatically, click it,
   then click **"Run workflow"** (this is the `workflow_dispatch` trigger).
3. Wait 3–6 minutes for it to finish (green checkmark).
4. Click into the completed run → scroll to **Artifacts** → download **`ytclient-debug-apk`**.
   This downloads a zip containing `app-debug.apk`.

## Step 5 — Install it on your phone

1. Move the extracted `app-debug.apk` onto your phone (e.g. via Google Drive, email to yourself,
   or a USB cable).
2. Open it with a file manager. Android will ask to allow installs from that app the first time —
   allow it, then tap **Install**.
3. Open "YT Client" from your app drawer.

---

## What works in this v1

- Search and trending video browsing via the official YouTube Data API
- Playback via YouTube's official embedded player (ads included, by design/ToS)
- Music Mode: tap the note icon top-right during playback to collapse the video into a
  compact audio-style player with play/pause controls

## What's not in yet (fair to flag since you mentioned wanting these)

- **YouTube Studio (your channel) features** — this needs OAuth sign-in (not just an API key),
  which is a separate setup step (Google Cloud OAuth consent screen + client ID). Since you said
  you have a channel, this is very doable as a v2 — just say the word and I'll add a sign-in flow
  plus a "My Channel" screen (your uploads, view counts, comments) using the same Data API.
- Playlists/subscriptions browsing for your own account (also needs OAuth, bundled with the above)
- Downloads/offline playback (not offered by the public API at all — YouTube doesn't expose this)

## A couple of honest limitations to know about

- **Music Mode and screen-off/background audio:** Android may throttle a WebView that's shrunk to
  near-zero size when the screen is off or the app is backgrounded, so background audio isn't
  fully reliable yet in this version — it works while the app is in the foreground. Making audio
  survive screen-off properly needs a foreground Service + MediaSession, which I can add next if
  that matters to you.
- **API quota**: heavy searching can hit the 10,000 units/day free cap. If you outgrow it, Google
  Cloud lets you request a quota increase for free.

Want me to add the OAuth-based "My Channel" (Studio-lite) screen next, or shore up the
background-audio behavior for Music Mode first?
