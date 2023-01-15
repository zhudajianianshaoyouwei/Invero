# Menu Serialize

```hocon
menu {
  type = CHEST
  rows = 6
  title = World
  
  title: [
    Hello
    Hello Invero
  ]
}
```

```hocon
title = "String"

title {

  value = [
    "Dynamic title",
    "Dynamic title 2",
    "Dynamic title 3",
  ]

  period = 20
  mode = random
  
}
```