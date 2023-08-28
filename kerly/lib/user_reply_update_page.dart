import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:kerly/item_detail_page.dart';
import 'package:provider/provider.dart';
import 'changenotifier.dart';

class UserReplyUpdatePage extends StatefulWidget {
  final int id;

  const UserReplyUpdatePage({Key? key, required this.id}) : super(key: key);

  @override
  State<UserReplyUpdatePage> createState() => _UserReplyUpdatePageState();
}

class _UserReplyUpdatePageState extends State<UserReplyUpdatePage> {
  bool _isLoading = false;
  String content = "";
  String productName = "";
  int _bid = 0;

  TextEditingController _contentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    loadData();
  }

  @override
  void dispose() {
    super.dispose();
    _contentController.dispose();
  }

  Future<void> loadData() async {
    setState(() {
      _isLoading = true;
    });

    try {
      String url = 'http://15.164.56.106:9007/reply-service/id/${widget.id}';

      final response = await http.get(
        Uri.parse(url),
        headers: {"Content-Type": "application/json"},
      );

      if (response.statusCode != 200) {
        throw Exception();
      }

      Map<String, dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));

      setState(() {
        productName = data['result']['productName'];
        _contentController.text = data['result']['content'];
        _bid = data['result']['bid'];
      });
    } catch (e) {
      print(e);
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
   return Scaffold(
        appBar: AppBar(
          title: Text("댓글 수정"),
          backgroundColor: Colors.pinkAccent,
        ),
        body: Center(
          child: Padding(
            padding: EdgeInsets.all(10),
            child: SingleChildScrollView(
              child: _isLoading
                  ? CircularProgressIndicator()
                  : Column(
                children: [
                  Text(
                    "상품: ${productName}",
                    style: TextStyle(fontSize: 30),
                  ),
                  SizedBox(height: 30),
                  TextField(
                    controller: _contentController,
                    style: TextStyle(fontSize: 25),
                  ),
                  SizedBox(height: 40),
                  ElevatedButton(
                    onPressed: () {
                      processUpdate();
                    },
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all<Color>(
                        Colors.pinkAccent,
                      ), // 원하는 색상으로 변경
                    ),
                    child: Text("수정"),
                  ),
                  SizedBox(height: 5),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pop(context);
                    },
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all<Color>(
                        Colors.grey,
                      ), // 원하는 색상으로 변경
                    ),
                    child: Text("취소"),
                  ),
                ],
              ),
            ),
          ),
        ),
      );
  }

  Future<void> processUpdate() async {
    String _content = _contentController.text;

    setState(() {
      _isLoading = true;
    });

    String url = 'http://15.164.56.106:9007/reply-service/update';

    try {
      Map<String, dynamic> body = {
        "content": _content,
        "id": widget.id,
        "username": Provider.of<MemberModel>(context, listen: false).username,
      };

      final response = await http.put(
        Uri.parse(url),
        headers: {
          "Content-Type": "application/json",
        },
        body: jsonEncode(body),
      );

      if (response.statusCode != 200) {
        throw Exception();
      }

      Map<String, dynamic> data = jsonDecode(utf8.decode(response.bodyBytes));

      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => ItemDetailPage(
            id: _bid,
            initialTabIndex: 1,
          ),
        ),
      );
    } catch (e) {
      print(e.toString());
      print('user_reply_updata_page');
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }
}
