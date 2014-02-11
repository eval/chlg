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
