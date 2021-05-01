package org.example.test;

public class FileVertx {
    public void file() {
//        FileSystem fs = vertx.fileSystem();
//
//        Future<FileProps> future = fs.props("/my_file.txt");
//
//        future.onComplete((AsyncResult<FileProps> ar) -> {
//            if (ar.succeeded()) {
//                FileProps props = ar.result();
//                System.out.println("File size = " + props.size());
//            } else {
//                System.out.println("Failure: " + ar.cause().getMessage());
//            }
//        });
    }

    public void compse() {
//        FileSystem fs = vertx.fileSystem();
//
//        Future<Void> future = fs
//                .createFile("/foo")
//                .compose(v -> {
//                    // When the file is created (fut1), execute this:
//                    return fs.writeFile("/foo", Buffer.buffer());
//                })
//                .compose(v -> {
//                    // When the file is written (fut2), execute this:
//                    return fs.move("/foo", "/bar");
//                });
    }
}
