package org.caoym.jjvm;

import com.sun.tools.classfile.*;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by caoyangmin on 2017/9/6.
 */
public class JvmClass {

    private ClassFile classFile;
    private HashMap< Pair<String,String>, JvmMethod> methods;

    static public JvmClass read(Path path) throws IOException, ConstantPoolException {
        return  new JvmClass(ClassFile.read(path));
    }

    /**
     *
     * @param classFile
     * @throws ConstantPoolException
     */
    private JvmClass(ClassFile classFile) throws ConstantPoolException {
        this.classFile = classFile;
        for (Method method : classFile.methods) {
            String name = method.getName(classFile.constant_pool);
            String desc = method.descriptor.getValue(classFile.constant_pool);
            methods.put(new Pair<>(name, desc), new JvmMethod(classFile, method));
        }
    }

    public JvmMethod getStaticMethod(String name, String desc) throws NoSuchMethodException, ConstantPoolException {
        JvmMethod method = methods.get(new Pair<>(name, desc));
        if(method == null){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not exist");
        }
        if(method.getAccessFlags().is(AccessFlags.ACC_STATIC|AccessFlags.ACC_PUBLIC)){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not a `public static` method");
        }
        return method;
    }

    public Object getStaticField(String name, String type) {
    }
}
