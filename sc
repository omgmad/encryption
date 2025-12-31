// SPDX-License-Identifier: BSD-3-Clause-Clear
pragma solidity ^0.8.24;

import { FHE, externalEuint32, euint32 } from "@fhevm/solidity/lib/FHE.sol";
import { ZamaEthereumConfig } from "@fhevm/solidity/config/ZamaConfig.sol";

/**
 * Энэ контракт нь 32 битийн нэг утгыг homomorphically шифрлэн хүлээн авна.
 */
contract EncryptSingleValue is ZamaEthereumConfig {
    // Encrypted state
    euint32 private _encryptedUint32;

    constructor() {}

    /**
     * Гадаад FHE шифрлэгдсэн утга + proof аваад захиалга хийдэг.
     */
    function initialize(
        externalEuint32 inputHandle,
        bytes calldata inputProof
    ) external {
        // Шифрлэгдсэн handle → contract-ын доторх euint32 convert
        _encryptedUint32 = FHE.fromExternal(inputHandle, inputProof);

        // Энэ encrypted value-д contract & caller-д decrypt хийх permission өгнө
        FHE.allowThis(_encryptedUint32);
        FHE.allow(_encryptedUint32, msg.sender);
    }

    /**
     * Шифрлэгдсэн утгыг contract-аас авах
     */
    function encryptedUint32() public view returns (euint32) {
        return _encryptedUint32;
    }
}
