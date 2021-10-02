# Basic lightweight deathswap plugin


## Commands

Command | Description | Required permission
--- | --- | ---
`/dsstart` | Start the deathswap | `ds.start`
`/dsstop` | Forcefully stop the deathswap | `ds.stop`
`/dsreload` | Reload the config file | `ds.reload`


## Permissions

Permission | Description
--- | ---
`ds.start` | Start the deathswap
`ds.stop` | Forcefully stop the deathswap
`ds.reload` | Reload the config file
`ds.op` | Treat the player as OP
`ds.*` | All permissions, except `ds.op`

## Config file

Setting | Description
--- | --- 
`min-time` | Minimum time between swaps *(seconds)*
`max-time` | Maximum time between swaps *(seconds)*
`fire-resistance-after-spawn` | For how long should the players get fire resistance after spawn *(seconds)*
`immunity-after-spawn` | For how long should the players get Resistance 255 after spawn *(seconds)*
`include-ops` | Wheter to add OP's or players with the `ds.op` permission to the game
