chlg
==========

Quickly fetch the changelog of a github repository.

Install
------------

```bash
npm install -g chlg
```

Usage
------------

```bash
$ chlg eval/chlg

  # 0.0.1 / 2014-02-09

  Initial release

# without repos-owner, the first repos found is used (feeling lucky?):
$ chlg devise

# nicely rendered
$ chlg devise | pandoc | lynx -stdin

# Help
$ chlg -h
```

Github's API, when used unauthenticated, has a low rate limit.
When you have a github token ([generate one here](https://github.com/settings/tokens/new)) and it's available as ENV-variable `GH_TOKEN`, the app will automatically use it.

Development
------------

- [Leiningen](https://github.com/technomancy/leiningen)
  `brew install leiningen`

```bash
$ lein cljsbuild auto
$ node chlg.js
```


Todo
---------

* add tests
