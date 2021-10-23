# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

### Changed
- Removed request authentication for remapped paths in `/data/product_categories/*` to make them publicly available to anonymous users
## [Unreleased]

### Deprecated
- Path `/public/categories` and `/public/categories/{parentId}`, mapped under `PublicCategoriesController` - Thanks @ParundeepSingh

## [1.0.4] - 2021-10-21

### Changed
- Clean up entities and remove redundant annotation values - Thanks to @trangntt-016

### Fix
- Fix resolving paging parameters where it would always get the default value.

### Security
- Restrict users from deleting their own account - Thanks to @trangntt-016


## [1.0.3] - 2021-09-23

### Changed
- `/public/receipt/{id}` updated with `id` parameter now called `token` and referencing sell payment token

### Fixed
- Fix default application parameters related to the checkout/payment service
    - Self-callback URL after clients navigates checkout
    - Success/Failure page URL to send customers to, after checkout
- Add missing sales filters required to fetch data during the checkout flow
- Add missing mock data into `data.sql` that is needed to invoke `/public/about`
- `ReceiptPojo` now uses `Instant` to represent dates, just like `Sell` and `SellPojo`


## [1.0.0] - 2021-09-20

First public version.
