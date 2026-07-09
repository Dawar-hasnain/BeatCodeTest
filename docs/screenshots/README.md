# Screenshots

Drop the app screenshots here with these exact filenames so the links in the
top-level `README.md` resolve:

| File | What to capture |
| --- | --- |
| `list.png` | The items list (portrait) — sorted seed items, a favorite star lit |
| `detail.png` | The detail screen of one item (portrait) |
| `add.png` | The add-item bottom sheet open, with the name field focused |
| `landscape.png` | **Landscape** — the two-pane list+detail layout side by side |

Optional extras you can also reference from the README if you want:
`empty.png` (empty state), `dark.png` (dark theme).

## Capturing

- On the device: Power + Volume Down, then pull the files over, **or**
- From a terminal with the device connected:
  ```bash
  adb exec-out screencap -p > list.png
  ```

## Keep them light

Phone PNGs are often 2–5 MB. Downscale to ~1080 px wide (a few hundred KB) before
committing so the repo stays small.
