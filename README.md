Author:        T. Stratton  
Date started:  11-NOV-2023  
GitHub ID:     POACH3  
Repo:          https://github.com/POACH3/BlockchainVisualizer


# Blockchain Visualizer
This visualizer is a more intuitive way to visualize crypto transaction activity on the Binance Smart Chain (BSC) blockchain.

The goal of this project is to gain more experience with graphs, creating a GUI, and using an API.

## Current Functionality
Data can be pulled from [BscScan](https://bscscan.com), utilizing the API. Call limiting functionality has been implemented to comply with the BscScan rate limit of 5 calls/second (for free API keys). Note that no limiting has been done for the 100,000 calls/day limit.

API key is protected with a secrets file.

Wallets can be created and queried for current balance as well as any information contained by transaction data associated with the wallet (date, block executed on, sender, receiver, amounts, gas price, etc...).

## Future Functionality
A user interface allowing the blockchain to be "surfed". A wallet address can be entered and then a digraph is generated with the wallets as nodes and where the directed edges show the net direction that capital is flowing. Thicker edges represent more transactions between wallets, greener edges represent more capital moved between wallets.
