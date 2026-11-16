# Submission guide – step by step

You submit two links: **GitHub URL** and **YouTube video URL**. Follow these steps in order.

---

## Step 1 – Open the project

1. Unzip `PowerBillEstimator.zip`.
2. Open Android Studio, then go to **File → Open** and choose the `PowerBillEstimator` folder (the one that contains `settings.gradle`).
3. Wait for **Gradle sync** to finish. The first time can take a few minutes because Android Studio downloads Gradle 8.9.
   - If Android Studio suggests upgrading the Android Gradle Plugin, click **Upgrade**. That is safe.
   - If it says the SDK is missing, open **Tools → SDK Manager** and install **Android 14 (API 34)**.

## Step 2 – Fill in your details (placeholders)

Open `app/src/main/res/values/strings.xml` and replace everything in `[BRACKETS]`:

| String | Replace with |
|---|---|
| `university_name` | Your university name |
| `faculty_name` | Your faculty or department (or delete the line in `activity_about.xml`) |
| `submission_date` | e.g. `Date: 20 / 11 / 2026` |
| `student_name` | Your full name |
| `student_id` | `Student ID: 12345678` |
| `course_code` / `course_name` | From your course outline |
| `github_url` | Your real repo link (after Step 4) |
| `copyright` | Your name and course code |

Do the same in `README.md` (it becomes your GitHub page).

## Step 3 – Add your university logo and your photo

1. Rename your logo to **`university_logo.png`** and your photo to **`student_photo.jpg`**. Use lowercase with no spaces.
2. In Android Studio, **delete** `app/src/main/res/drawable/university_logo.xml` and `student_photo.xml`. These are the placeholders. If you keep them, the build fails with a "duplicate resource" error.
3. Copy your two images into `app/src/main/res/drawable/`.
4. Run the app (green ▶ button) and open **About** to check them.

## Step 4 – Push to GitHub from Android Studio

1. **File → Settings → Version Control → GitHub**: click **+** and log in to your GitHub account.
2. **Git → GitHub → Share Project on GitHub** (on older versions: **VCS → Share Project on GitHub**).
3. Repository name: `PowerBillEstimator`. Select **Public**, then click **Share**.
4. In the commit dialog, keep all files ticked and click **Add**. Android Studio creates the repo and pushes the code.
5. Copy the repo URL (e.g. `https://github.com/yourname/PowerBillEstimator`) into `github_url` in `strings.xml`.
6. Commit and push again with **Git → Commit** (Ctrl+K), tick **Push** or press Ctrl+Shift+K.

## Step 5 – Generate the signed APK

1. **Build → Generate Signed App Bundle / APK** and choose **APK**.
2. Click **Create new…** under Key store path:
   - Save the keystore **outside** the project folder, e.g. `Documents/powerbill-key.jks`.
   - Set a password, alias `powerbill`, and fill in at least your name in *First and Last Name*.
   - **Keep this file and password safe.** You need them for any future update.
3. Choose the **release** build variant and click **Create**.
4. The APK is created at `app/release/app-release.apk`.
5. On GitHub, open your repo → **Releases → Create a new release**. Use tag `v1.0`, attach `app-release.apk`, then click **Publish**.

The `.gitignore` already excludes `.jks` files, so your key is never uploaded.

## Step 6 – Screenshots (optional, but they make the GitHub page look complete)

Take five screenshots from the emulator (camera icon in the emulator toolbar) and save them in a `docs/` folder as `main.png`, `result.png`, `history.png`, `detail.png` and `about.png`. Save your logo there as `docs/university_logo.png` too, then push.

## Step 7 – Record the YouTube demo (light mode)

Make sure the emulator or phone is in **light mode** (Settings → Display → Dark theme OFF). The app is light-only, but the system UI should be light as well.

Record with OBS Studio or the Windows Game Bar (Win+G). Suggested script, 4–6 minutes:

| Time | Show | Say |
|---|---|---|
| 0:00 | Your face or a title slide | Name, student ID, course, "PowerBill Estimator" |
| 0:20 | Home screen app icon | "Custom icon, app name in the title bar, custom blue and amber theme" |
| 0:40 | Main page | Explain the instructions card, month dropdown, number-only units field, rebate slider |
| 1:20 | Leave units empty, tap Calculate | "Error message when the field is empty" |
| 1:35 | Type 1500 | "Live error: units must be 1–1000" |
| 1:50 | Enter 467, rebate 5%, Calculate | Read the result: total RM 163.17, final RM 155.01. Show the breakdown and match it to the assignment table |
| 2:30 | Add 2–3 more months (e.g. 150, 551) | "Each result is saved to the SQLite database" |
| 3:00 | Open History | "ListView shows month and final cost only" |
| 3:20 | Tap a row, then Edit | Change the units, Save changes, show the recalculated cost, go back and show the list updated |
| 4:00 | Delete one bill | Show the confirmation dialog, then the list updated |
| 4:20 | About page | Logo, photo, details, tap the GitHub link and show it opens |
| 4:50 | GitHub repo page | Show the code and the release APK |
| 5:10 | End | "Thank you" |

Upload it to your YouTube channel as **Unlisted** or **Public**, not Private, and copy the link.

## Step 8 – Submit

- GitHub URL: `https://github.com/yourname/PowerBillEstimator`
- YouTube URL: `https://youtu.be/...`

---

### Rubric checklist

- [x] Input/Output: 3 inputs (spinner, number field, **slider**), 2 outputs formatted as RM
- [x] Database: all inputs and outputs stored; ListView; detail page; edit; delete
- [x] Layout: custom theme, title bar, custom icons
- [x] About page: picture, name, ID, course, copyright, clickable URL, instructions
- [x] Good design: error messages, helpful notices (toasts, helper text), instructions
- [ ] Your details filled in (Step 2–3)
- [ ] Pushed to GitHub (Step 4)
- [ ] Signed APK (Step 5)
- [ ] YouTube video (Step 7)
