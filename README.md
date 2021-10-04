# Yet Another Deathswap Plugin

**This plugin supports 1.16.5! It is not guaranteed it will work with earlier/later versions of Spigot! (I will test 1.17 soon-ish)**

## Commands

| Command                                        | Description                   | Required permission |
| ---------------------------------------------- | ----------------------------- | ------------------- |
| `/dsstart (optional - user1, user2, user3...)` | Start the deathswap           | `ds.start`          |
| `/dsstop`                                      | Forcefully stop the deathswap | `ds.stop`           |
| `/dsreload`                                    | Reload the config file        | `ds.reload`         |

## Permissions

| Permission  | Command/Functionality           |
| ----------- | ------------------------------- |
| `ds.start`  | /dsstart                        |
| `ds.stop`   | /dsstop                         |
| `ds.reload` | /dsreload                       |
| `ds.op`     | Treat the player as OP          |
| `ds.*`      | All permissions, except `ds.op` |

## Config file

| Setting                       | Description                                                                 |
| ----------------------------- | --------------------------------------------------------------------------- |
| `min-time`                    | Minimum time between swaps _(seconds)_                                      |
| `max-time`                    | Maximum time between swaps _(seconds)_                                      |
| `fire-resistance-after-spawn` | For how long should the players get fire resistance after spawn _(seconds)_ |
| `immunity-after-spawn`        | For how long should the players get Resistance 255 after spawn _(seconds)_  |
| `include-ops`                 | Wheter to add OP's or players with the `ds.op` permission to the game       |
