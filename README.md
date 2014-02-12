chlg
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
$ node out/chlg.js eval/chlg

  # 0.0.1 / 2014-02-09

  Initial release

# Help
$ node out/chlg.js -h
```

Github's API, when used unauthenticated, has a low rate limit.
When you have a github token ([generate one here](https://github.com/settings/tokens/new)) and it's available as ENV-variable `GH_TOKEN`, the app will automatically use it.


Todo
---------

* publish as npm module
* add tests
* find a repository only by name (e.g. `chlog devise`)
