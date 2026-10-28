# EasyBank — Java OOP Semester Project

A console-based banking application built with Java 17+ and Maven. The source is split into proper packages and includes the concepts from Practical 1–14: records, enums, encapsulation, inheritance, polymorphism, interfaces, regex validation, parsing, annotations/reflection, checked exceptions, synchronized account operations, executor threads, file persistence, NIO reporting, collections, generics, and JUnit tests.

## Requirements
- JDK 17 or newer
- Maven 3.8+ (VS Code Java Extension Pack is recommended)

## Open in VS Code / Codespaces
1. Extract the project ZIP, or open this folder in VS Code.
2. Ensure VS Code detects the installed JDK 17+ and Maven.
3. Open the terminal in the folder containing `pom.xml`.

## Run
```bash
mvn clean package
java -jar target/swiftbank-app-1.0.0.jar
```
The JAR manifest points to `app.EasyBank`. The application creates `data/accounts.bin`, `data/logs/transactions.log`, and `data/report.txt` as needed. Account data is loaded at startup and saved when you select Exit.

## Run tests
```bash
mvn test
```
Tests cover deposit balance changes, rejected withdrawals, and concurrent deposits.

## Packages
- `app`: menu and program entry point
- `model`: Account hierarchy, Customer, records, enums, and interfaces
- `model.annotation`: runtime annotations (`@Id`, `@Positive`, `@MaxLength`, `@Audit`)
- `exception`: checked banking exceptions
- `util`: validation, command parsing, statements, persistence, logging, reports, reflection validation
- `service`: `BankService`, generic `Repository<T>`, `AccountStore`, `TransactionProcessor`, `AccountWorker`, and wait/notify `TransactionBuffer`

## Menu
1. Open account (Savings / Current / Fixed Deposit)
2. Deposit
3. Withdraw
4. Transfer
5. List accounts
6. Statement
7. End-of-day report
8. Exit and save

## Beginner notes
- A `SavingsAccount` must retain its minimum balance (default ₹500).
- A `CurrentAccount` in this demo has an overdraft limit of ₹10,000.
- A `FixedDepositAccount` does not allow withdrawals.
- Deposit/withdraw amounts must be positive. Banking errors are reported as friendly messages.
- This is an educational console project, **not production banking software**. It does not implement authentication, encryption, database-backed durability, or regulatory controls. Java object serialization should not be used for untrusted files.

## Practical mapping
- P1: `EasyBank`, `BankInfo`, `MenuOption`, menu loop and switch
- P2–P3: `Customer`, `Account`, constructors, encapsulation, `toString`, `equals`, `hashCode`, `Address`, cloning
- P4: `Validator`, `Command`, `CommandParser`, `StatementFormatter`
- P5–P6: abstract account types, interfaces, lambda and anonymous class, packages, runnable JAR
- P7: runtime annotations and `AnnotationValidator`
- P8: exception hierarchy, transfer and try-with-resources demo
- P9–P10: synchronized account methods and `TransactionProcessor`
- P11: serialization, append-only logs and NIO report generation
- P12–P13: `AccountStore`, `ConcurrentHashMap`, `Comparable`, generic repository and bounded generic method
- P14: `BankService`, connected menu, persistence, report and JUnit tests

The deliberately broken race-condition and deadlock examples are not run in the banking application because they are intentionally unsafe. `AccountWorker` supports the concurrent-deposit practical, `TransactionBuffer<T>` demonstrates producer-consumer `wait()`/`notifyAll()`, and `Account.transfer()` locks accounts in account-number order to avoid deadlock.



