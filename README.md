****
https://stackoverflow.com/questions/28389006/how-to-decompile-to-java-files-intellij-idea

IDEA反编译工具路径 D:\DevelopTool\IntelliJ IDEA 2019.1.3\plugins\java-decompiler\lib
将.class打包成jar文件  
cd C:\Users\yuxia\Desktop\ocl\ocle-2.0.4\classes 
jar cvf ocle.jar .

进行反编译
cd D:\DevelopTool\IntelliJ IDEA 2019.1.3\plugins\java-decompiler\lib
java -cp "java-decompiler.jar"  org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler -hdc=0 -dgs=1 -rsy=1 -rbr=1 -lit=1 -nls=1 -mpm=60 C:\Users\yuxia\Desktop\ocl\ocle-2.0.4\classes\ocle.jar C:\Users\yuxia\Desktop\ocl\ocle-2.0.4\out_arc\

由于反编译不是完美的，生成的代码依然会有点问题，需要进行对应的修改
类似于
```
Method method =  clz.getMethod("createNew" + type, new Class[] {
                 ((Object) (new Object[0])).getClass()
         });
//  Method method = clz.getMethod("createNew" + type, new Class[]{Object.class});
//  result = method.invoke(creator, new Object[]{context});
result = method.invoke(creator, new Object[] {
                 new Object[] {
                         context
                 }
});
```
