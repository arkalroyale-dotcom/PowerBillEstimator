<p align="center">
  <img src="docs/logo_qiu.png" alt="Qaiwan International University" height="70"/>
  &nbsp;&nbsp;&nbsp;
  <img src="docs/logo_uitm.png" alt="Universiti Teknologi MARA" height="70"/>
</p>

<h2 align="center">Qaiwan International University · Universiti Teknologi MARA</h2>
<p align="center">
  Mobile Technology – Individual Assignment (20%)<br/>
  Course: Mobile Application<br/>
  Student: Arkal Akram Ahmed &nbsp;|&nbsp; Student ID: QIU 23-0337<br/>
  Date: 17 / 11 / 2026
</p>

---

# ⚡ PowerBill Estimator

An Android app that estimates a monthly electricity bill using block tariff rates, applies a rebate, and stores every result in an offline SQLite database.

**Demo video:** [YOUTUBE LINK]
**Signed APK:** see the [Releases](https://github.com/arkalroyale-dotcom/PowerBillEstimator/releases) page

## Features

| Requirement | How the app meets it |
|---|---|
| Inputs | Month (dropdown, Jan–Dec), units used (number field, 1–1000 kWh), rebate (slider, 0–5%) |
| Outputs | Total charges and final cost after rebate, formatted as `RM 0.00`, shown on the same page with a block-by-block breakdown |
| Local database | SQLite (`SQLiteOpenHelper`) stores month, units, rebate, total charges and final cost |
| List View | History page lists **Month** and **Final cost** only |
| Detail page | Tap a row to view all details, **Edit** (recalculates) or **Delete** (with confirmation) |
| Layout | Custom blue/amber theme, app name in the title bar, custom adaptive lightning-bolt icon, title-bar action icons |
| Good design | Number-only input, red error messages, live validation, helper text, toasts, on-screen instructions, tariff table |
| About page | University logos and names, student photo, full name, student ID, course code and name, copyright, clickable website URL, full instructions |

## Tariff

| Block | Rate (sen/kWh) |
|---|---|
| 1 – 200 kWh | 21.8 |
| 201 – 300 kWh | 33.4 |
| 301 – 600 kWh | 51.6 |
| 601 – 1000 kWh | 54.6 |

`Final cost = Total charges − (Total charges × Rebate %)`

Verified against the assignment samples: 150 → RM 32.70, 250 → RM 60.30, 467 → RM 163.17, 551 → RM 206.52, 115 → RM 25.07.

## How to use

1. Select the month.
2. Enter the units used (1–1000 kWh).
3. Drag the rebate slider (0–5%).
4. Tap **Calculate & Save**. The result appears below and is saved.
5. Tap the clock icon to open **History**, then tap a bill to view, edit or delete it.
6. Tap the (i) icon for the **About** page.

## Screenshots

| Main | Result | History | Detail | About |
|---|---|---|---|---|
| ![](docs/main.png) | ![](docs/result.png) | ![](docs/history.png) | ![](docs/detail.png) | ![](docs/about.png) |

## Tech

Java · Android SDK (min 26, target 34) · SQLite · ListView · no third-party libraries

## Project structure

```
app/src/main/java/com/arkal/powerbill/
├── MainActivity.java      // inputs, calculation, result, save
├── HistoryActivity.java   // ListView of saved bills
├── DetailActivity.java    // view / edit / delete one bill
├── AboutActivity.java     // student info, website link, instructions
├── BillCalculator.java    // block tariff logic
├── DatabaseHelper.java    // SQLite CRUD
├── BillAdapter.java       // ListView adapter (month + final cost)
├── Bill.java              // data model
└── UiUtils.java           // validation helpers
```

---
© 2026 Arkal Akram Ahmed. All rights reserved.
