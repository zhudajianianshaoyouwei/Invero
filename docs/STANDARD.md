# 多态序列化

- 简之可**简**

```yaml
title: 'Invero Menu'

layout: |-
  #########
  |       |
  |   *   |
  |       |
  #########

items:
  '#':
    material: gray stained glass pane
  '|':
    material: cyan stained glass pane
  '*':
    material: beacon
    name: '<red><bold>CLICK TP TO SPAWN'
    action: |-
      command "spawn"
```

- 标准规范（底层对象的序列化）
- 实际应用的写法选择中取决个人偏好

```hocon
menu {
  title {}
  rows: 6
  type: CHEST
  virtual: false
  override-player-inventory: false
}

activators {
  items {}
  commands {}
  chat {}
}

events {
  open {}
  opened {}
  close {}
}

panels = [
  {
    layout = [
      ---------
      |       |
      |       |
      |       |
      ---------
    ]
    items {
      '-' {
        material: stone
      }
    }
  }
]
```

### 解释

- 通过 `menu` 或 `title` 根节点声明此文件以菜单加载
- 若根节点存在 `title`, `rows`, `type` 等，自动转化为 menu 下的 JsonObject
- 若根节点存在 `layout`, 自动寻找其他 Panel 节点，本菜单自动解析为单 Panel 实例


Icon 简易规范

```hocon
icon {
  material {}
  name {}
  lore {}

  amount {}
  damage {}
  model {}
  color {}
  glow {}
  enchantments {}
  flags {}
  banner {}
  unbreakable {}
  nbt {}

  slot {}

  frames = []
  frames_prop {}

  action {}
  sub {}
}
```

Icon 标准规范

```hocon
icon {

  display {
    material {}
    name {}
    lore {}

    amount {}
    damage {}
    model {}
    color {}
    glow {}
    enchantments {}
    flags {}
    banner {}
    unbreakable {}
    nbt {}

    slot {}
  }

  frames = [
    {
      display: struc
    }
  ]

  frames_prop {}

  action {}

  sub {}
}
```