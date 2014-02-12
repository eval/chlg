chlog
==========

Show the changelog of a github repository.

Requirements
------------

- [Leiningen](https://github.com/technomancy/leiningen)
  `brew install leiningen`
- [Nodejs](https://github.com/joyent/node)
  `brew install nodejs`

Usage
------------

```bash
# Build it
$ lein cljsbuild once

# Run it
$ node out/chlog.js eval/chlog

  # 0.0.1 / 2014-02-09

  Initial release

# Help
$ node out/chlog.js -h
```

Github's API, when used unauthenticated, has a low rate limit.
When you have a github token ([generate one here](https://github.com/settings/tokens/new)) and it's available as ENV-variable `GH_TOKEN`, the app will automatically use it.
