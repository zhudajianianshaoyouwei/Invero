> menu operators

- menu title set "xxx"
- menu title pause
- menu title resume

> panel operators

- panel scroll <direction>
- panel shift by 0x1
- panel filter set "filter keyword"
- panel filter get
- panel icons

> icon operators

- icon relocate at 0
- icon update
- icon task {task_type} pause
- icon task {task_type} resume
- icon item
    - amount set/get
    - name set/get
    - ...

> context data

```
仅在单个菜单会话中有效的临时变量
```

- context variable get "key"
- context variable set "key" to "value"

```
服务器重启前都保留的临时持久化变量
```

- context variable static get "key"

```
存储到 Invero 默认数据库中的玩家个人变量及全局变量
```

- context globaldata get "key"
- context playerdata get "key"