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
        ];
      };
    };
}
