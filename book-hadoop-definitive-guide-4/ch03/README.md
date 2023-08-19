# 3 HDFS

```
hdfs dfs -copyFromLocal input/docs/quangle.txt quangle.txt &&
hdfs dfs -ls
```
```
Found 1 items
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
hdfs dfs -copyToLocal quangle.txt quangle.copy.txt &&
md5 input/docs/quangle.txt quangle.copy.txt
```
```
MD5 (input/docs/quangle.txt) = e7891a2627cf263a079fb0f18256ffb2
MD5 (quangle.copy.txt) = e7891a2627cf263a079fb0f18256ffb2
```
```
hdfs dfs -mkdir books &&
hdfs dfs -ls
```
```
Found 2 items
drwxr-xr-x   - df supergroup          0 2023-08-18 15:37 books
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
hdfs dfs -ls file:///
```
```
Found 18 items
----------   1 root admin          0 2023-07-11 04:56 file:///.file
drwxr-xr-x   - root wheel         64 2023-07-11 04:56 file:///.vol
drwxrwxr-x   - root admin       4640 2023-08-18 03:43 file:///Applications
drwxr-xr-x   - root wheel       2432 2023-07-28 12:21 file:///Library
drwxr-xr-x   - root wheel        320 2023-07-11 04:56 file:///System
drwxr-xr-x   - root admin        224 2023-08-15 15:46 file:///Users
drwxr-xr-x   - root wheel         96 2023-08-15 18:40 file:///Volumes
drwxr-xr-x   - root wheel       1248 2023-07-11 04:56 file:///bin
drwxr-xr-x   - root wheel         64 2021-09-18 02:26 file:///cores
dr-xr-xr-x   - root wheel       8190 2023-08-15 15:39 file:///dev
drwxr-xr-x   - root wheel       2528 2023-08-17 17:36 file:///etc
dr-xr-xr-x   - root wheel          1 2023-08-15 15:39 file:///home
drwxr-xr-x   - root wheel        128 2023-05-09 10:03 file:///opt
drwxr-xr-x   - root wheel        192 2023-08-15 15:39 file:///private
drwxr-xr-x   - root wheel       2048 2023-07-11 04:56 file:///sbin
drwxrwxrwt   - root wheel        512 2023-08-18 14:38 file:///tmp
drwxr-xr-x   - root wheel        352 2023-07-11 04:56 file:///usr
drwxr-xr-x   - root wheel       1152 2023-08-15 15:39 file:///var
```
```
vim URLCat.java
```
```java
import java.lang.Object;

public class URLCat extends Object {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	public static void main (String[] args) throws Exception {
		InputStream in = null;
		try {
			in = new URL(args[0]).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	}
}
```
```
 hadoop jar hadoop-examples.jar URLCat hdfs://localhost:9000/user/df/quangle.txt
```
or
```
hadoop jar hadoop-examples.jar URLCat hdfs:///user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```
```
vim FileSystemCat.java
```
```java
import java.lang.Object;

public class FileSystemCat extends Object {
	public static void main (String[] args) throws Exception {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		InputStream in = null;
		try {
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	}
}
```
```
hadoop jar hadoop-examples.jar FileSystemCat hdfs://localhost:9000/user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
```
```
vim FileSystemDoubleCat.java
```
```java
import java.lang.Object;

public class FileSystemDoubleCat extends Object {
	public static void main (String[] args) throws Exception {
    String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FSDataInputStream in = null;
		try {
			in = fs.open(new Path(uri));
			IOUtils.copyBytes(in, System.out, 4096, false);
			in.seek(0);
			IOUtils.copyBytes(in, System.out, 4096, false);
		}
		finally {
			IOUtils.closeStream(in);
		}
	} 
}
```
```
 hadoop jar hadoop-examples.jar FileSystemDoubleCat hdfs://localhost:9000/user/df/quangle.txt
```
```
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat.
On the top of the Crumpetty Tree
The Quangle Wangle sat,
But his face you could not see,
On account of his Beaver Hat
```
```
vim FileCopyWithProgress.java
```
```java
import java.lang.Object;

public class FileCopyWithProgress extends Object {
	public static void main (String[] args) throws Exception {
		String localSrc = args[0];
		String dst      = args[1];
		InputStream in  = new BufferedInputStream(new FileInputStream(localSrc));
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dst), conf);
		OutputStream out = fs.create(new Path(dst), new Progressable() {
			public void progress () {
				System.out.print(".");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
	}
}
```
```
hadoop jar hadoop-examples.jar FileCopyWithProgress input/docs/1400-8.txt hdfs://localhost:9000/user/df/1400-8.txt
```
```
..................
```
```
hdfs dfs -ls
```
```
Found 3 items
-rw-r--r--   1 df supergroup    1033751 2023-08-18 22:28 1400-8.txt
drwxr-xr-x   - df supergroup          0 2023-08-18 15:37 books
-rw-r--r--   1 df supergroup        119 2023-08-18 15:31 quangle.txt
```
```
vim ShowFileStatusTest.java
```
```java
import java.lang.Object;

public class ShowFileStatusTest extends Object {
	private MiniDFSCluster cluster;
	private FileSystem fs;

	@Before
	public void setUp () throws Exception {
		Configuration conf = new Configuration();
		if (System.getProperty("test.build.data") == null) {
			System.getProperty("test.build.data", "/tmp");
		}
		cluster = new MiniDFSCluster.Builder(conf).build();
		fs = cluster.getFileSystem();
		OutputStream out = fs.create(new Path("/dir/file"));
		out.write("content".getBytes("UTF-8"));
		out.close();
	}

	@After
	public void tearDown () throws IOException {
		if (fs != null) { fs.close(); }
		if (cluster != null) { cluster.shutdown(); }
	}

	@Test (expected = FileNotFoundException.class)
	public void throwsFileNotFoundForNonExistentFile () throws IOException {
		fs.getFileStatus(new Path("no-such-file"));
	}

	@Test
	public void fileStatusForFile () throws IOException {
		Path file = new Path("/dir/file");
		FileStatus stat = fs.getFileStatus(file);
		assertThat(stat.getPath().toUri().getPath(), is("/dir/file"));
		assertThat(stat.isDirectory(), is(false));
		assertThat(stat.getLen(), is(7L));
		assertThat(stat.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(stat.getReplication(), is((short) 1));
		assertThat(stat.getBlockSize(), is(128 * 1024 * 1024L));
		assertThat(stat.getOwner(), is(System.getProperty("user.name")));
		assertThat(stat.getGroup(), is("supergroup"));
		assertThat(stat.getPermission().toString(), is("rw-r--r--"));
	}

	@Test
	public void fileStatusForDirectory () throws IOException {
		Path dir = new Path("/dir");
		FileStatus stat = fs.getFileStatus(dir);
		assertThat(stat.getPath().toUri().getPath(), is("/dir"));
		assertThat(stat.isDirectory(), is(true));
		assertThat(stat.getLen(), is(0L));
		assertThat(stat.getModificationTime(), is(lessThanOrEqualTo(System.currentTimeMillis())));
		assertThat(stat.getReplication(), is((short) 0));
		assertThat(stat.getBlockSize(), is(0L));
		assertThat(stat.getOwner(), is(System.getProperty("user.name")));
		assertThat(stat.getGroup(), is("supergroup"));
		assertThat(stat.getPermission().toString(), is("rwxr-xr-x"));
	}
}
```
```
vim ListStatus.java
```
```java
import java.lang.Object;

public class ListStatus extends Object {
	public static void main (String[] args) throws Exception {
    String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}
		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path p : listPaths) {
			System.out.println(p);
		}
	}
}
```
```
hadoop jar hadoop-examples.jar ListStatus hdfs://localhost:9000/ hdfs://localhost:9000/user/df
```
```
hdfs://localhost:9000/user
hdfs://localhost:9000/user/df/1400-8.txt
hdfs://localhost:9000/user/df/books
hdfs://localhost:9000/user/df/quangle.txt
```