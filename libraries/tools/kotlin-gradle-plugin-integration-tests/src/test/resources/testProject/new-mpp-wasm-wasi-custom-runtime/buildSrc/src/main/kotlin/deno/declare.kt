package deno

import org.jetbrains.kotlin.gradle.targets.wasm.runtime.dsl.runtime
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import java.nio.file.Path
import kotlin.io.path.absolutePathString

fun KotlinWasmWasiTargetDsl.deno() {
    runtime(
        "deno",
        "1.46.3"
    ) {
        download { os: String, arch: String, version: String ->
            val denoSuffix = when {
                os.lowercase().contains("linux") -> when {
                    arch == "x86_64" -> "x86_64-unknown-linux-gnu"
                    arch == "arm" || arch.startsWith("aarch") -> "aarch64-unknown-linux-gnu"
                    else -> error("unsupported os type ${os} and arch $arch")
                }
                os.lowercase().contains("mac") -> when {
                    arch == "x86_64" -> "x86_64-apple-darwin"
                    arch == "arm" || arch.startsWith("aarch") -> "aarch64-apple-darwin"
                    else -> error("unsupported os type ${os} and arch $arch")
                }
                os.lowercase().contains("windows") -> when {
                    arch == "x86_64" -> "x86_64-pc-windows-msvc"
                    else -> error("unsupported os type ${os} and arch $arch")
                }
                else -> error("unsupported os type ${os} and arch $arch")
            }

            "https://github.com/denoland/deno/releases/download/v$version/deno-$denoSuffix.zip"
        }

        executable { _: String, _: String, _: String, installationDir: Path? ->
            installationDir?.resolve("deno")?.normalize()?.absolutePathString() ?: "deno"
        }

        runArgs { isolationDir: Path, entry: Path ->
            val denoMjs = prepareFile(
                isolationDir.toFile(),
                "startDeno.mjs",
                entry.toFile()
            )

            denoArgs(denoMjs)
        }

        testArgs { isolationDir: Path, entry: Path ->
            val denoMjs = prepareFile(
                isolationDir.toFile(),
                "runUnitTestsDeno.mjs",
                entry.toFile()
            )

            denoArgs(denoMjs)
        }
    }
}