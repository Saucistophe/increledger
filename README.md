# Increledger, a robust incremental game server

![](https://github.com/Saucistophe/increledger/actions/workflows/maven.yml/badge.svg)
![](https://byob.yarr.is/Saucistophe/increledger/Coverage)

```
      ______ ______    (\
    _/      Y      \_  | \
   // Incre | Ledger\\ \ /
  //        |        \\ \ 
 //________.|.________\\)(
`----------`-`---------(__)
```

## Goals

Inspired by the amazing [EvolveIdle](https://github.com/pmotschmann/Evolve), I thought it was too easy and tempting to cheat because all data is stored on the front-end.
I also always wanted to create something where the rules can be opaque.

So I came up with this 100%-backend driven, (hopefully) infalsifiable record of your progress in an incremental game, where rules are private and can only be known by progressing.

## Principle

The game server runs with private (RSA) keys and uses them to sign every instance of the game. Every modification you want to make on your game requires to post (valid) actions to the server; the server processes the actions, computes the resources produced since your game was issued, and returns a new signed copy.

This means that no modification can be done outside of the game's rules, and that nothing is stored on the server.