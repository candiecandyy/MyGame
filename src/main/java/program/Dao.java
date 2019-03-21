package program;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by admin on 2017/5/24
 * 数据库操作方法.
 */
public class Dao {

    /**
     * 根据名称查询用户
     *
     * @param name 名称
     * @return 用户实体
     */
    public Users findUserByName(String name) {
        Users users = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("rank.jas")));
            String str = null;
            while ((str = br.readLine()) != null) {
                str = decode(str);
                if (str.matches(name + ",.*,.*")) {
                    users = new Users();
                    String[] arr = str.split(",");
                    users.setName(arr[0]);
                    users.setScore(Integer.valueOf(arr[1]));
                    users.setTime(arr[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        Connection conn = null;
//        try {
//            conn = program.DBUtil.getConnection();
//            String sql = "SELECT * FROM users WHERE name=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setString(1,name);
//            ResultSet rs = ps.executeQuery();
//            if(rs.next()){
//                users = new program.Users(rs.getString("name"),
//                        rs.getInt("score"),rs.getString("time"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            program.DBUtil.close(conn);
//        }
        return users;
    }

    /**
     * 修改用户
     *
     * @param users 用户实体
     */
    public void modifyUsers(Users users) {
        String name = users.getName();
        BufferedReader br = null;
        List<String> list = new ArrayList<>();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createDate = smt.format(new Date());
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("rank.jas")));
            String str = null;
            while ((str = br.readLine()) != null) {
                str = decode(str);
                if (str.matches(name + ",.*,.*")) {
                    str = name + "," + String.valueOf(users.getScore()) + "," + users.getTime() + "," + createDate;
                }
                list.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("rank.jas"), "utf-8"), true);
            for (String str : list) {
                pw.println(encode(str));
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
//        Connection conn = null;
//        try {
//            conn = program.DBUtil.getConnection();
//            String sql = "UPDATE users SET currentime=?,score=?,time=? WHERE name=?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps = conn.prepareStatement(sql);
//            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
//            ps.setInt(2, users.getScore());
//            ps.setString(3, users.getTime());
//            ps.setString(4, users.getName());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            program.DBUtil.close(conn);
//        }
    }

    /**
     * 添加用户
     *
     * @param users 用户实体
     */
    public void saveUsers(Users users) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("rank.jas", true), "utf-8"), true);
            SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createDate = smt.format(new Date());
            String str = users.getName() + "," + String.valueOf(users.getScore()) + "," + users.getTime() + "," + createDate;
            pw.println(encode(str));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
//        Connection conn = null;
//        try {
//            conn = program.DBUtil.getConnection();
//            String sql = "INSERT INTO users VALUES(?,?,?,?)";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, users.getName());
//            ps.setInt(2, users.getScore());
//            ps.setString(3, users.getTime());
//            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            program.DBUtil.close(conn);
//        }
    }

    /**
     * 查询所有用户
     *
     * @return 用户实体集合
     */
    public List<Users> findAllUsers() {
        List<String> list = new ArrayList<>();
        List<Users> userss = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("rank.jas")));
            String str = null;
            while ((str = br.readLine()) != null) {
                str = decode(str);
                list.add(str);
            }
            list.sort((o1, o2) -> {
                SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String[] arr1 = o1.split(",");
                String[] arr2 = o2.split(",");
                try {
                    return (int) (smt.parse(arr1[3]).getTime() - smt.parse(arr2[3]).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            });
            for (String str1 : list) {
                String[] arr = str1.split(",");
                Users users = new Users();
                users.setName(arr[0]);
                users.setScore(Integer.valueOf(arr[1]));
                users.setTime(arr[2]);
                userss.add(users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        Connection conn = null;
//        try {
//            conn = program.DBUtil.getConnection();
//            String sql = "SELECT * FROM users ORDER BY currentime DESC";
//            Statement smt = conn.createStatement();
//            ResultSet rs = smt.executeQuery(sql);
//            while (rs.next()){
//                program.Users users = new program.Users(rs.getString("name"),
//                        rs.getInt("score"),rs.getString("time"));
//                list.add(users);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
//            program.DBUtil.close(conn);
//        }
        return userss;
    }

    public String encode(String str) {
        str = CyptoUtil.encode(str);
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] >= 97 && arr[i] <= 122) || (arr[i] >= 65 && arr[i] <= 90)
                    || (arr[i] >= 48 && arr[i] <= 57)) {
                arr[i] = (char) (arr[i] + 1);
            }
            if (arr[i] == '=') {
                arr[i] = '@';
            }
        }
        return String.valueOf(arr);
    }

    public String decode(String str) {
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] >= 98 && arr[i] <= 123) || (arr[i] >= 66 && arr[i] <= 91)
                    || (arr[i] >= 49 && arr[i] <= 58)) {
                arr[i] = (char) (arr[i] - 1);
            }
            if (arr[i] == '@') {
                arr[i] = '=';
            }
        }
        return CyptoUtil.decode(String.valueOf(arr));
    }

    public static void main(String[] args) {
        System.out.println(new Dao().decode("d3csODIwLDAwOjA3OjA4LDIwMTgtMTEtMzAgMTM6NDQ6MTc="));
    }
}
