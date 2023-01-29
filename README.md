# Invero
Versatile GUIs

---

### Menu
```yaml
title: 'Spawn Menu'

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
![SPAWN](https://user-images.githubusercontent.com/35389235/215314320-b98cbca8-5542-4a69-88b1-e969c2c68ff3.png)

---

### Window

```kotlin
packetChestWindow(rows = 6, "Loop Scroll (horizontally)") {

    scroll(9 to 5, direction = ScrollDirection.HORIZONTAL, tail = -1) {

        repeat(10) {
            val columTexture = randomMaterial()

            insertColum { buildItem(columTexture) }
        }

    }

    nav(9 to 1) {
        val target = firstScrollPanel()

        scrollController(target, -1, 0, Material.CYAN_STAINED_GLASS_PANE) {
            modify { name = "§3← LEFT" }
        }
        scrollController(target, +1, 8, Material.LIME_STAINED_GLASS_PANE) {
            modify { name = "§a→ RIGHT" }
        }

        item(Material.GRAY_STAINED_GLASS_PANE).fillup()
    }

}
```
![scroll](https://user-images.githubusercontent.com/35389235/211839566-1718ffa8-4190-4532-8c39-352ec6629417.gif)


---

### Capacity

https://user-images.githubusercontent.com/35389235/210974601-22d94b34-f1b9-4ed2-bedf-467b24940acc.mp4

https://user-images.githubusercontent.com/35389235/210974606-67b12fd0-49ee-48fd-b936-332f4379a19d.mp4

https://user-images.githubusercontent.com/35389235/211138960-b2084523-d13b-4305-8837-0bf97198166a.mp4
