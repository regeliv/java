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

      # Apparently, jdtls by default uses some old java, this is
      # a fix for that
      jdtlsJava25 = pkgs.jdt-language-server.overrideAttrs (old: {
        postPatch = (old.postPatch or "") + ''
          sed -i -E "s|java_executable = '/nix/store/[^']*-openjdk-[^']*/bin/java'|java_executable = '${pkgs.openjdk25}/bin/java'|" bin/jdtls.py
        '';
      });
    in
    {
      devShells.${system}.default = pkgs.mkShell {
        packages = [
          pkgs.openjdk25
          pkgs.maven
          jdtlsJava25
          pkgs.postgresql
          pkgs.podman
        ];

        shellHook = ''
          export PGDATA=$PWD/.postgres
          export PGHOST=$PGDATA
          export DOCKER_HOST="unix://$XDG_RUNTIME_DIR/podman/podman.sock"
          export TESTCONTAINERS_RYUK_DISABLED=true

          systemctl --user start podman.socket || true

          if [ ! -S "$XDG_RUNTIME_DIR/podman/podman.sock" ]; then
            echo "Podman socket not found at $XDG_RUNTIME_DIR/podman/podman.sock"
            echo "Run: systemctl --user enable --now podman.socket"
          fi

          if [ ! -d "$PGDATA" ]; then
            initdb --no-locale --encoding=UTF8
          fi

          pg_ctl start -l "$PGDATA/postgres.log" -o "--unix_socket_directories='$PGDATA'"
          trap "pg_ctl stop" EXIT
        '';
      };
    };
}
