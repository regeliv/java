{
  description = "Java 25 development environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  };

  outputs =
    { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs {
        inherit system;

      };
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = [
          pkgs.openjdk25
          pkgs.maven
          pkgs.jdt-language-server
          pkgs.postgresql
        ];

        shellHook = ''
          export PGDATA=$PWD/.postgres
          export PGHOST=$PGDATA

          if [ ! -d "$PGDATA" ]; then
            initdb --no-locale --encoding=UTF8
          fi

          pg_ctl start -l "$PGDATA/postgres.log" -o "--unix_socket_directories='$PGDATA'"
          trap "pg_ctl stop" EXIT
        '';
      };
    };
}
