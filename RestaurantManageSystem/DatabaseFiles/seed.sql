INSERT INTO employee(id, name, mobile, email, address) VALUES (1, 'Anupam', '9842568104', 'anupam@gmail.com', 'Example Address, Test Road, Test City - 411038'), (2, 'Achyut', '9626662624', 'ashukla@gmail.com', 'Test Address, Address Road, Demo City - 411004'), (3, 'Antriksh', '9525562304', 'antriksh@gmail.com', 'Example Address, Test Road, Test City - 411038'), (4, 'Ojas', '8444475404', 'ojasinamdar101@gmail.com', 'Demo Building, Example Colony, Test Road - 411009'), (5, 'Moubani', '9762560204', 'moubani@gmail.com', 'Test Road, Example City - 411038');

INSERT INTO waiter(salary, employee_id) VALUES ('20000', 1), ('20000', 3), ('20000', 5);

INSERT INTO admin(employee_id) VALUES (2), (4);

INSERT INTO customer(id, name, mobile, email, address) VALUES (1, 'AnupamCustomer', '6842568104', 'anupam@gmail.com', 'Example Address, Test Road, Test City - 411038'), (2, 'AchyutCustomer', '6626662624', 'ashukla@gmail.com', 'Test Address, Address Road, Demo City - 411004'), (3, 'AntrikshCustomer', '6525562304', 'antriksh@gmail.com', 'Example Address, Test Road, Test City - 411038'), (4, 'OjasCustomer', '6444475404', 'ojasinamdar101@gmail.com', 'Demo Building, Example Colony, Test Road - 411009'), (5, 'MoubaniCustomer', '6762560204', 'moubani@gmail.com', 'Test Road, Example City - 411038');

INSERT INTO item(name, price, type) VALUES ('Veg Burger', 60, 'food'), ('Nonveg Burger', 100, 'food'), ('Pizza', 200, 'food'), ('Soda', 60, 'beverage'), ('Pina Colada', 150, 'beverage');

INSERT INTO "order"(id, total, status, waiter_id, customer_id) VALUES (1, 2000, 'received', 3, 3), (2, 1000, 'ongoing', 2, 2), (3, 2000, 'received', 3, 2), (4, 2000, 'complete', 1, 3), (5, 2000, 'complete', 2, 3);

INSERT INTO order_item(order_id, item_id,  quantity) VALUES (5, 1, 10), (5, 2, 10), (4, 2, 5), (3, 5, 5), (2, 1, 1), (2, 2, 1), (2, 3, 5), (1, 4, 1);